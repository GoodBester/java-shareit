package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingReturnDtoJsonTest {

    @Autowired
    private JacksonTester<BookingReturnDto> json;


    @Test
    void testBookingDto() throws Exception {
        LocalDateTime time = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        String stringTime = time.toString();
        String stringTime1 = time.plusDays(1).toString();

        BookingReturnDto bookingReturnDto = new BookingReturnDto(
                1L,
                null,
                time,
                time.plusDays(1),
                null,
                Status.APPROVED
        );

        JsonContent<BookingReturnDto> result = json.write(bookingReturnDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(stringTime);
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(stringTime1);
        assertThat(result).extractingJsonPathStringValue("$.booker").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(Status.APPROVED.toString());
    }
}
