package br.com.bitewisebytes.simpletwittersecurity.twittter;

import java.util.List;

public record FeedDTO(List<FeedItemDTO> feedItemList, int page, int pageSize, int totalPages ,Long totalElements) {
}
