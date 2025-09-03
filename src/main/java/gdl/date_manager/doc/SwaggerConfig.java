package gdl.date_manager.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gerenciador de Datas")
                        .description("Sistema para gerenciar a validade de produtos de um supermercado")
                        .version("0.1.0")
                        .contact(new Contact()
                                .name("Caique Castro")
                                .email("caiquecastrosilva@gmail.com")
//                                .url("")
                        )
                );
    }
}