package shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.client.BaseClient;
import shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Service
@Slf4j
public class ItemRequestService extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestService(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> add(long userId, ItemRequestDto itemRequestDto) {
        return post("", userId, null, itemRequestDto);
    }

    public ResponseEntity<Object> get(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getUsersRequests(long userId, long index, long size) {

        Map<String, Object> parameters = Map.of(
                "from", index,
                "size", size
        );

        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getById(long requestId, long userId) {
        return get("/" + requestId, userId);
    }
}
