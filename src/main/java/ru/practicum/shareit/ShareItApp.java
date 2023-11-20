package ru.practicum.shareit;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exceptions.ValidationException;


@SpringBootApplication
public class ShareItApp {

    public static void main(String[] args) {
        SpringApplication.run(ShareItApp.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        return mapper;
    }

    public static Pageable getPage(int page, int size) {
        if (page < 0 || size < 0 || (page == 0 && size == 0)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Incorrect parameters");
        }
        return PageRequest.of(page > 0 ? page / size : 0, size);
    }

}
