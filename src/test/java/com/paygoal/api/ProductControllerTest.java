package com.paygoal.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paygoal.api.common.dtos.Product.CreateProductRequest;
import com.paygoal.api.common.dtos.Product.ProductDto;
import com.paygoal.api.common.dtos.Product.UpdateProductRequest;
import com.paygoal.api.common.exceptions.ProductNotFoundException;
import com.paygoal.api.common.model.Product;
import com.paygoal.api.common.services.ProductInternalService;
import com.paygoal.api.common.services.ProductService;
import com.paygoal.api.v1.ProductController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;


@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@MockBean
	ProductInternalService productInternalService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	public void testGetProductById() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setName("Test Product");
		product.setPrice(100L);
		product.setQuantity(10L);
		product.setDescription("Test Description");

		Mockito.when(productService.getById(1L)).thenReturn(ProductDto.of(product));
		Mockito.when(productService.getById(2L)).thenThrow(new ProductNotFoundException());

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test Product")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Test Description")));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/2"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testCreateProduct() throws Exception {
		CreateProductRequest createProductRequest = new CreateProductRequest();
		createProductRequest.setName("Test Product");
		createProductRequest.setPrice(100L);
		createProductRequest.setQuantity(10L);
		createProductRequest.setDescription("Test Description");

		Product product = new Product();
		product.setId(1L);
		product.setName("Test Product");
		product.setPrice(100L);
		product.setQuantity(10L);
		product.setDescription("Test Description");

		Mockito.when(productService.createProduct(Mockito.any(CreateProductRequest.class))).thenReturn(ProductDto.of(product));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.content(objectMapper.writeValueAsString(createProductRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test Product")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Test Description")));
	}

	@Test
	public void testUpdateProduct() throws Exception {
		CreateProductRequest createProductRequest = new CreateProductRequest();
		createProductRequest.setName("Test Product");
		createProductRequest.setPrice(100L);
		createProductRequest.setQuantity(10L);
		createProductRequest.setDescription("Test Description");

		Product product = new Product();
		product.setId(1L);
		product.setName("Test Product");
		product.setPrice(100L);
		product.setQuantity(10L);
		product.setDescription("Test Description");

		Mockito
				.when(productService.updateProduct(Mockito.anyLong(), Mockito.any(UpdateProductRequest.class)))
				.thenReturn(ProductDto.of(product));

		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", 1L)
				.content(objectMapper.writeValueAsString(createProductRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test Product")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Test Description")));
	}

	@Test
	public void testDeleteProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", 1L))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetAllProducts() throws Exception {
		Product product1 = new Product();
		product1.setId(1L);
		product1.setName("Test Product 1");
		product1.setPrice(100L);
		product1.setQuantity(10L);
		product1.setDescription("Test Description 1");

		Product product2 = new Product();
		product2.setId(2L);
		product2.setName("Test Product 2");
		product2.setPrice(200L);
		product2.setQuantity(20L);
		product2.setDescription("Test Description 2");

		Mockito.when(productService.getAllProducts()).thenReturn(List.of(ProductDto.of(product1), ProductDto.of(product2)));

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
		Product product = new Product();
		product.setId(1L);
		product.setName("Test Product");
		product.setPrice(100L);
		product.setQuantity(10L);
		product.setDescription("Test Description");

		Mockito.when(productService.getByName(Mockito.anyString())).thenReturn(ProductDto.of(product));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/name/{name}", "Test Product"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test Product")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(10)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Test Description")));
	}

	@Test
	public void testGetProductPage() throws Exception {
		Product product1 = new Product();
		product1.setId(1L);
		product1.setName("Test Product 1");
		product1.setPrice(100L);
		product1.setQuantity(10L);
		product1.setDescription("Test Description 1");

		Product product2 = new Product();
		product2.setId(2L);
		product2.setName("Test Product 2");
		product2.setPrice(200L);
		product2.setQuantity(20L);
		product2.setDescription("Test Description 2");

		Page<ProductDto> productPage = new PageImpl<>(List.of(ProductDto.of(product1), ProductDto.of(product2)));

		Mockito.when(productService.getPage(Mockito.any(Pageable.class))).thenReturn(productPage);

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
