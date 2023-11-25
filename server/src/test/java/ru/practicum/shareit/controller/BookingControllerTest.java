package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    LocalDateTime time = LocalDateTime.now().plusDays(1);

    private BookingReturnDto bookingReturnDto = new BookingReturnDto(
            1L,
            null,
            time,
            time,
            null,
            Status.APPROVED
    );

    private BookingDto bookingDto = new BookingDto(
            1L,
            1L,
            time,
            time,
            Status.APPROVED
    );

    @Test
    void saveNewBooking() throws Exception {
        when(bookingService.create(any(), anyLong()))
                .thenReturn(bookingReturnDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingReturnDto);
        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void setApprovedBooking() throws Exception {
        when(bookingService.setApproved(any(), anyLong(), any()))
                .thenReturn(bookingReturnDto);
        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getAllBooking() throws Exception {
        when(bookingService.getAllBookingFromUser(anyLong(), any(), any(), any()))
                .thenReturn(List.of(bookingReturnDto, bookingReturnDto, bookingReturnDto));
        mvc.perform(get("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(bookingReturnDto.getId()))
                .andExpect(jsonPath("$[0].status").value(bookingReturnDto.getStatus().toString()));
    }


}
