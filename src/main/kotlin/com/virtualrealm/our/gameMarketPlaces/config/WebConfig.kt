import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://127.0.0.1:5501", "http://localhost:8080", "http://localhost:5501")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
    }
}
