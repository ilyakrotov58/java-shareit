package shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.booking.dto.BookingDto;
import shareit.client.BaseClient;
import shareit.exceptions.ValidateException;

import java.util.Map;

@Slf4j
@Service
@Transactional
public class BookingService extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingService(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> add(BookingDto bookingDto, long userId) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null
        || bookingDto.getStart().isAfter(bookingDto.getEnd())
        || bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidateException("Booking start date can't be after end date");
        }
        return post("", userId, null, bookingDto);
    }

    public ResponseEntity<Object> approve(long bookingId, boolean approved, long userId) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved=" + approved, userId, parameters);
    }

    public ResponseEntity<Object> get(long bookingId, long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookings(String state, long userId, long index, long size) {

        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", index,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllBookingsByItems(String state, long userId, long index, long size) {

        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", index,
                "size", size
        );

        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}
