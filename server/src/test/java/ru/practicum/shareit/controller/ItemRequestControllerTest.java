package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReturnDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    LocalDateTime time = LocalDateTime.now();
    private ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "description",
            time
    );

    private ItemRequestReturnDto itemRequestReturnDto = new ItemRequestReturnDto(
            1L,
            1L,
            "description",
            time,
            null
    );


    @Test
    void saveNewItemRequest() throws Exception {
        when(itemRequestService.addRequest(anyLong(), any()))
                .thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    void getUserItemRequest() throws Exception {
        when(itemRequestService.getRequests(anyLong()))
                .thenReturn(List.of(itemRequestReturnDto, itemRequestReturnDto, itemRequestReturnDto));
        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(itemRequestReturnDto.getId()))
                .andExpect(jsonPath("$[0].description").value("description"));
    }


    @Test
    void getAllItemRequest() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), any()))
                .thenReturn(List.of(itemRequestReturnDto, itemRequestReturnDto, itemRequestReturnDto));
        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(itemRequestReturnDto.getId()))
                .andExpect(jsonPath("$[0].description").value("description"));
    }

    @Test
    void getItemRequest() throws Exception {
        when(itemRequestService.getRequest(anyLong(), anyLong()))
                .thenReturn(itemRequestReturnDto);
        mvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("description"));
    }

}
