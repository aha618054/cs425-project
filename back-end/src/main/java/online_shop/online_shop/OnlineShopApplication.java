package online_shop.online_shop;

import online_shop.online_shop.domain.Category;
import online_shop.online_shop.domain.Product;
import online_shop.online_shop.domain.User;
import online_shop.online_shop.dto.request.UserRegisterRequest;
import online_shop.online_shop.repository.CategoryRepository;
import online_shop.online_shop.repository.ProductRepository;
import online_shop.online_shop.repository.UserRepository;
import online_shop.online_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import online_shop.online_shop.property.FileStorageProperties;

import java.util.Optional;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "online_shop.online_shop.repository")
@EntityScan(basePackages = "online_shop.online_shop.domain")
@EnableConfigurationProperties({ FileStorageProperties.class })
public class OnlineShopApplication implements CommandLineRunner {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(OnlineShopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		addAdmin();
		Category electronics = addCategoryIfNotExist("Digital");
		Category clothing = addCategoryIfNotExist("Clothing");
		Category beauty = addCategoryIfNotExist("Beauty");

		addProductIfNotExist("Laptop", "High performance laptop designed for gaming and heavy workloads, featuring the latest Intel i7 processor and a powerful GTX 1660 graphics card.", 999.99, electronics);
		addProductIfNotExist("Smartphone", "Latest model smartphone with a 6.5-inch OLED display, 128GB storage, and a 48MP camera to capture stunning photos.", 799.99, electronics);
		addProductIfNotExist("Smartwatch", "Stylish smartwatch with fitness tracking, heart rate monitoring, and notifications directly from your phone.", 199.99, electronics);

		addProductIfNotExist("T-shirt", "Soft and breathable cotton T-shirt with a classic fit, ideal for casual wear or outdoor activities.", 19.99, clothing);
		addProductIfNotExist("Jeans", "Premium quality denim jeans with a modern slim fit, available in various washes and styles to suit every occasion.", 49.99, clothing);
		addProductIfNotExist("Leather Jacket", "Genuine leather jacket that combines comfort and style, perfect for evening outings or casual everyday wear.", 129.99, clothing);

		addProductIfNotExist("Shampoo", "Gentle, sulfate-free shampoo with natural ingredients, leaving your hair soft, shiny, and nourished.", 8.99, beauty);
		addProductIfNotExist("Face Cream", "Anti-aging face cream with vitamin C, hydrating and firming the skin while reducing fine lines and wrinkles.", 24.99, beauty);
		addProductIfNotExist("Lipstick", "Bold and long-lasting lipstick available in various shades to enhance your smile and complement your look.", 15.99, beauty);
	}

	private void addAdmin() throws Exception {
		Optional<User> user = userRepository.findByEmail("admin@example.com");
		if (user.isEmpty()){
			UserRegisterRequest userAuthRequest = new UserRegisterRequest("admin", "admin@example.com", "test@123");
			userService.registerNewAdmin(userAuthRequest);
		}
	}

	private Category addCategoryIfNotExist(String categoryName) {
		// Check if the category with the specified name already exists
		Optional<Category> existingCategory = categoryRepository.findByName(categoryName);
		Category newCategory = new Category();
		newCategory.setName(categoryName);

		if (existingCategory.isPresent()) {
			System.out.println("Category with name '" + categoryName + "' already exists. Doing nothing.");
		} else {
			categoryRepository.save(newCategory);
			System.out.println("Added category: " + newCategory);
		}
		return newCategory;
	}

	private void addProductIfNotExist(String name, String description, double price, Category category) {
		Optional<Product> existingProduct = productRepository.findByName(name);
		if (existingProduct.isPresent()) {
			System.out.println("Product with name '" + name + "' already exists. Doing nothing.");
		} else {
			Product product = new Product(name, description, price, category);
			productRepository.save(product);
			System.out.println("Added: " + product);
		}
	}
}
