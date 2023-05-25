package com.paygoal.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paygoal.api.common.dtos.Product.CreateProductRequest;
import com.paygoal.api.common.dtos.Product.ProductDto;
import com.paygoal.api.common.dtos.Product.UpdateProductRequest;
import com.paygoal.api.common.exceptions.ProductAlreadyExistsException;
import com.paygoal.api.common.exceptions.ProductNotFoundException;
import com.paygoal.api.common.model.Product;
import com.paygoal.api.common.services.ProductService;
import com.paygoal.api.v1.ProductController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;


@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	ObjectMapper objectMapper;

	static Product product1;
	static Product product2;

	@BeforeAll
	public static void setup() {
		product1 = new Product();
		product1.setId(1L);
		product1.setName("Test Product 1");
		product1.setPrice(100L);
		product1.setQuantity(10L);
		product1.setDescription("Test Description 1");

		product2 = new Product();
		product2.setId(2L);
		product2.setName("Test Product 2");
		product2.setPrice(200L);
		product2.setQuantity(20L);
		product2.setDescription("Test Description 2");
	}

	@Test
	public void testGetProductById() throws Exception {
		Mockito.when(productService.getById(1L)).thenReturn(ProductDto.of(product1));
		Mockito.when(productService.getById(2L)).thenThrow(new ProductNotFoundException());

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test Product 1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Test Description 1")));
	}

	@Test
	public void testGetProductByIdShouldThrowProductNotFound() throws Exception {
		Mockito.when(productService.getById(1L)).thenThrow(new ProductNotFoundException());
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/1"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testCreateProduct() throws Exception {
		CreateProductRequest createProductRequest = new CreateProductRequest();
		createProductRequest.setName("Test Product");
		createProductRequest.setPrice(100L);
		createProductRequest.setQuantity(10L);
		createProductRequest.setDescription("Test Description");

		Mockito.when(productService.create(Mockito.any(CreateProductRequest.class))).thenReturn(ProductDto.of(product1));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.content(objectMapper.writeValueAsString(createProductRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test Product 1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Test Description 1")));
	}

	@Test
	public void testCreateProductShouldThrowProductAlreadyExistsException() throws Exception {
		CreateProductRequest createProductRequest = new CreateProductRequest();
		createProductRequest.setName("Test Product");
		createProductRequest.setPrice(100L);
		createProductRequest.setQuantity(10L);
		createProductRequest.setDescription("Test Description");

		Mockito.when(productService.create(Mockito.any(CreateProductRequest.class))).thenThrow(new ProductAlreadyExistsException());

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.content(objectMapper.writeValueAsString(createProductRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isConflict());
	}

	@Test
	public void testUpdateProduct() throws Exception {
		CreateProductRequest createProductRequest = new CreateProductRequest();
		createProductRequest.setName("Test Product");
		createProductRequest.setPrice(100L);
		createProductRequest.setQuantity(10L);
		createProductRequest.setDescription("Test Description");

		Mockito
				.when(productService.update(Mockito.anyLong(), Mockito.any(UpdateProductRequest.class)))
				.thenReturn(ProductDto.of(product1));

		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", 1L)
				.content(objectMapper.writeValueAsString(createProductRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test Product 1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Test Description 1")));
	}

	@Test
	public void testUpdateProductShouldThrowProductAlreadyExistsException() throws Exception {
		UpdateProductRequest updateProductRequest = new UpdateProductRequest();
		updateProductRequest.setName("Test Product");
		updateProductRequest.setPrice(100L);
		updateProductRequest.setQuantity(10L);
		updateProductRequest.setDescription("Test Description");

		Mockito
				.when(productService.update(Mockito.anyLong(), Mockito.any(UpdateProductRequest.class)))
				.thenThrow(new ProductAlreadyExistsException());
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", 1L)
				.content(objectMapper.writeValueAsString(updateProductRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isConflict());
	}

	@Test
	public void testUpdateProductShouldThrowProductNotFoundException() throws Exception {
		UpdateProductRequest updateProductRequest = new UpdateProductRequest();
		updateProductRequest.setName("Test Product");
		updateProductRequest.setPrice(100L);
		updateProductRequest.setQuantity(10L);
		updateProductRequest.setDescription("Test Description");

		Mockito
				.when(productService.update(Mockito.anyLong(), Mockito.any(UpdateProductRequest.class)))
				.thenThrow(new ProductNotFoundException());
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", 1L)
				.content(objectMapper.writeValueAsString(updateProductRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testDeleteProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", 1L))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetAllProducts() throws Exception {
		Mockito.when(productService.getAll()).thenReturn(List.of(ProductDto.of(product1), ProductDto.of(product2)));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Test Product 1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].price", Matchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity", Matchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is("Test Description 1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Test Product 2")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].price", Matchers.is(200)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity", Matchers.is(20)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].description", Matchers.is("Test Description 2")));
	}

	@Test
	public void testGetProductByName() throws Exception {
		Mockito.when(productService.getByName(Mockito.anyString())).thenReturn(ProductDto.of(product1));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/name/{name}", "Test Product 1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test Product 1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Test Description 1")));
	}

	@Test
	public void testGetProductByNameShouldThrowNotFoundException() throws Exception {
		Mockito.when(productService.getByName(Mockito.anyString())).thenThrow(new ProductNotFoundException());

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/name/{name}", "Test Product"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testGetProductPage() throws Exception {
		Page<ProductDto> productPage = new PageImpl<>(List.of(ProductDto.of(product1), ProductDto.of(product2)));

		Mockito.when(productService.findAll(Mockito.any(Pageable.class))).thenReturn(productPage);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/page")
				.param("pageNo", "0")
				.param("pageSize", "10")
				.param("sortBy", "id")
				.param("sortDirection", "ASC"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", Matchers.is("Test Product 1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price", Matchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity", Matchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description", Matchers.is("Test Description 1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id", Matchers.is(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name", Matchers.is("Test Product 2")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].price", Matchers.is(200)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].quantity", Matchers.is(20)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].description", Matchers.is("Test Description 2")));
	}
}
