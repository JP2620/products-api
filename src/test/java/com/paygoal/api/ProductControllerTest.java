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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
}
