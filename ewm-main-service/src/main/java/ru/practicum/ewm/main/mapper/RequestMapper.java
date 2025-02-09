//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ru.practicum.ewm.main.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.main.dto.RequestDto;
import ru.practicum.ewm.main.entity.Event;
import ru.practicum.ewm.main.entity.Request;
import ru.practicum.ewm.main.entity.User;

@Component
public class RequestMapper {

    public RequestDto requestToDto(Request request) {
        if (request == null) {
            return null;
        } else {
            RequestDto requestDto = new RequestDto();
            requestDto.setEvent(this.requestEventId(request));
            requestDto.setRequester(this.requestRequesterId(request));
            requestDto.setId(request.getId());
            requestDto.setStatus(request.getStatus());
            requestDto.setCreated(request.getCreated());
            return requestDto;
        }
    }

    private Long requestEventId(Request request) {
        if (request == null) {
            return null;
        } else {
            Event event = request.getEvent();
            if (event == null) {
                return null;
            } else {
                Long id = event.getId();
                return id == null ? null : id;
            }
        }
    }

    private Long requestRequesterId(Request request) {
        if (request == null) {
            return null;
        } else {
            User requester = request.getRequester();
            if (requester == null) {
                return null;
            } else {
                Long id = requester.getId();
                return id == null ? null : id;
            }
        }
    }
}
