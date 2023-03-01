package shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.client.BaseClient;
import shareit.item.dto.CommentDto;
import shareit.item.dto.ItemDto;

import java.util.Map;

@Service
@Slf4j
@Transactional
public class ItemService extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemService(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> add(ItemDto itemDto, long userId) {
        return post("", userId, null, itemDto);
    }

    public ResponseEntity<Object> edit(ItemDto itemDto, long itemId, long userId) {
        return patch("/" + itemId, userId, null, itemDto);
    }

    public ResponseEntity<Object> getById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public void deleteById(long itemId, long userId) {
        delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllByText(String text, long index, long size) {

        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", index,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> getAll(long userId, long index, long size) {

        Map<String, Object> parameters = Map.of(
                "from", index,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, null, commentDto);
    }
}
