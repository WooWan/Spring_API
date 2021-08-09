package com.example.springAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemsService {

    private final ItemsRepository itemsRepository;

    public Long save(ItemsSaveDto itemsSaveDto) {
        return itemsRepository.save(itemsSaveDto.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public ItemsResponseDto findById(Long id) {
         Item item= itemsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 없습니다."));
        return new ItemsResponseDto(item);
    }
}
