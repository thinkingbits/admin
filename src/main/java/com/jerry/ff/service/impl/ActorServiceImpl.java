package com.jerry.ff.service.impl;

import com.jerry.ff.exception.BusinessException;
import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.dto.ActorDTO;
import com.jerry.ff.model.entity.Actor;
import com.jerry.ff.model.vo.ActorVO;
import com.jerry.ff.repository.ActorRepository;
import com.jerry.ff.service.ActorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    @Override
    @Transactional
    @CacheEvict(value = "actors", allEntries = true)
    public ActorVO createActor(ActorDTO actorDTO) {
        if (actorDTO == null) {
            throw new BusinessException(400, "Actor information cannot be empty");
        }

        if (!StringUtils.hasText(actorDTO.getName())) {
            throw new BusinessException(400, "Actor name cannot be empty");
        }

        if (actorRepository.existsByName(actorDTO.getName().trim())) {
            throw new BusinessException(400, "Actor name already exists");
        }

        Actor actor = Actor.builder()
                .name(actorDTO.getName().trim())
                .originalName(StringUtils.hasText(actorDTO.getOriginalName()) ? actorDTO.getOriginalName().trim() : null)
                .avatarUrl(StringUtils.hasText(actorDTO.getAvatarUrl()) ? actorDTO.getAvatarUrl().trim() : null)
                .description(StringUtils.hasText(actorDTO.getDescription()) ? actorDTO.getDescription().trim() : null)
                .build();

        Actor savedActor = actorRepository.save(actor);
        log.info("Created new actor: {}", savedActor.getName());
        return convertToActorVO(savedActor);
    }

    @Override
    @Transactional
    @CacheEvict(value = "actors", allEntries = true)
    public ActorVO updateActor(Long id, ActorDTO actorDTO) {
        if (id == null || actorDTO == null) {
            throw new BusinessException(400, "Actor ID and information cannot be empty");
        }

        if (!StringUtils.hasText(actorDTO.getName())) {
            throw new BusinessException(400, "Actor name cannot be empty");
        }

        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "Actor not found"));

        String newName = actorDTO.getName().trim();
        if (!actor.getName().equals(newName) && actorRepository.existsByName(newName)) {
            throw new BusinessException(400, "Actor name already exists");
        }

        actor.setName(newName);
        actor.setOriginalName(StringUtils.hasText(actorDTO.getOriginalName()) ? actorDTO.getOriginalName().trim() : null);
        actor.setAvatarUrl(StringUtils.hasText(actorDTO.getAvatarUrl()) ? actorDTO.getAvatarUrl().trim() : null);
        actor.setDescription(StringUtils.hasText(actorDTO.getDescription()) ? actorDTO.getDescription().trim() : null);

        Actor updatedActor = actorRepository.save(actor);
        log.info("Updated actor: {}", updatedActor.getName());
        return convertToActorVO(updatedActor);
    }

    @Override
    @Transactional
    @CacheEvict(value = "actors", allEntries = true)
    public void deleteActor(Long id) {
        if (id == null) {
            throw new BusinessException(400, "Actor ID cannot be empty");
        }

        if (!actorRepository.existsById(id)) {
            throw new BusinessException(404, "Actor not found");
        }
        
        actorRepository.deleteById(id);
        log.info("Deleted actor: {}", id);
    }

    @Override
    @Cacheable(value = "actors", key = "'actor_' + #id")
    public ActorVO getActor(Long id) {
        if (id == null) {
            throw new BusinessException(400, "Actor ID cannot be empty");
        }

        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "Actor not found"));
        
        return convertToActorVO(actor);
    }

    @Override
    @Cacheable(value = "actors", key = "'all_page_' + #pageable.pageNumber")
    public Page<ActorVO> getAllActors(Pageable pageable) {
        if (pageable == null) {
            throw new BusinessException(400, "Page parameters cannot be empty");
        }

        Page<Actor> actorPage = actorRepository.findAll(pageable);
        return convertToActorVOPage(actorPage);
    }

    @Override
    public Page<ActorVO> searchActors(String keyword, Pageable pageable) {
        if (pageable == null) {
            throw new BusinessException(400, "Page parameters cannot be empty");
        }

        if (!StringUtils.hasText(keyword)) {
            return getAllActors(pageable);
        }

        Page<Actor> actorPage = actorRepository.searchActors(keyword.trim(), pageable);
        return convertToActorVOPage(actorPage);
    }

    @Override
    @Cacheable(value = "actors", key = "'film_' + #filmId")
    public List<ActorVO> getActorsByFilm(Long filmId) {
        if (filmId == null) {
            throw new BusinessException(400, "Film ID cannot be empty");
        }

        List<Actor> actors = actorRepository.findByFilmId(filmId);
        return actors.stream()
                .map(this::convertToActorVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActorVO> getActorsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<Actor> actors = actorRepository.findByIdIn(ids.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        return actors.stream()
                .map(this::convertToActorVO)
                .collect(Collectors.toList());
    }

    private ActorVO convertToActorVO(Actor actor) {
        if (actor == null) {
            return null;
        }

        return ActorVO.builder()
                .id(actor.getId())
                .name(actor.getName())
                .originalName(actor.getOriginalName())
                .avatarUrl(actor.getAvatarUrl())
                .description(actor.getDescription())
                .createAt(actor.getCreateAt())
                .updateAt(actor.getUpdateAt())
                .build();
    }

    private Page<ActorVO> convertToActorVOPage(Page<Actor> actorPage) {
        if (actorPage == null) {
            return Page.empty();
        }

        List<ActorVO> actorVOList = actorPage.getContent().stream()
                .map(this::convertToActorVO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(actorVOList, actorPage.getPageable(), actorPage.getTotalElements());
    }
} 