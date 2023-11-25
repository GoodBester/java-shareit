package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReturnDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Autowired
    private JacksonTester<ItemReturnDto> json1;

    @Autowired
    private JacksonTester<CommentDto> json2;

    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto(
                1L,
                "name",
                "description",
                true,
                null,
                1L
        );

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.owner").isEqualTo(null);
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void testItemReturnDto() throws Exception {
        ItemReturnDto itemReturnDto = new ItemReturnDto(
                1L,
                "name",
                "description",
                true,
                null,
                1,
                null,
                null,
                null
        );

        JsonContent<ItemReturnDto> result = json1.write(itemReturnDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.owner").isEqualTo(null);
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }


    @Test
    void testCommentDto() throws Exception {
        LocalDateTime time = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
        String stringTime = time.toString();

        CommentDto commentDto = new CommentDto(
                1L,
                "author_name",
                "text",
                time
        );

        JsonContent<CommentDto> result = json2.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("author_name");
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(stringTime);
    }
}
