package com.lav.libtool.service;

import com.lav.libtool.dto.reader.ReaderCreateRequestDTO;
import com.lav.libtool.dto.reader.ReaderResponseDTO;
import com.lav.libtool.dto.reader.ReaderUpdateRequestDTO;
import com.lav.libtool.entity.Reader;
import com.lav.libtool.exceptions.ReaderAlreadyExistsException;
import com.lav.libtool.exceptions.ReaderNotFoundException;
import com.lav.libtool.mappers.ReaderMapper;
import com.lav.libtool.repository.ReaderRepository;
import com.lav.libtool.util.NormalizerEmail;
import com.lav.libtool.util.NormalizerPhone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository repository;

    @Override
    public ReaderResponseDTO create(ReaderCreateRequestDTO newReader) {
        // TODO: в логах не стоит логировать чувствительные данные, такие как email и телефон. Даже имя и фамилию,
        //  а email и телефон точно не стоит логировать или только в нормализованном виде (например, маскировать часть данных).
        log.info("Creating new reader: {} {}", newReader.firstName(), newReader.lastName());
        var normEmail = NormalizerEmail.normalize(newReader.email());
        var normPhone = NormalizerPhone.normalize(newReader.phone());
        if(repository.existsByEmail(normEmail)) throw new ReaderAlreadyExistsException(normEmail);

        Reader reader = new Reader(
                newReader.firstName(),
                newReader.lastName(),
                normEmail,
                normPhone);
        // TODO: ты создаешь reader, сохраняешь его, а потом мапишь в DTO reader вместо savedReader.
        //  лучше маппить сохраненного savedReader.
        //  Проблема в том, что в hibernate ID может быть сгенерирован только при сохранении сущности.
        //  Если ты мапишь до сохранения, то в DTO будет id = null, а после сохранения — уже сгенерированный ID.
        var savedReader = repository.save(reader);

        log.info("Reader created successfully with ID: {}", savedReader.getId());
        // TODO: в теории если мы будем работать с несколькими инстансами,
        //  то может возникнуть Race condition,
        //  когда два запроса на создание читателя с одинаковым email будут одновременно проверять existsByEmail и оба увидят,
        //  что такого email нет, и оба создадут читателя с одинаковым email.
        //  Чтобы избежать этого, можно добавить уникальный индекс на поле email
        //  в базе данных и обрабатывать исключение при попытке вставить дубликат.
        //  Но в рамках этого задания можно оставить как есть, так как это уже будет выходить за рамки текущей реализации.
        //  реализуется это не очень сложно, нужно просто добавить уникальный индекс на email в базе данных и обработать исключение при попытке вставить дубликат.
        //  и обработать DataIntegrityViolationException
        //  и если уникальный индекс в БД поймал дубль — переводим в понятную ошибку домена (твой кастомный exception)
        return ReaderMapper.toReaderResponseDTO(reader);
    }

    @Override
    @Transactional(readOnly = true)
    public ReaderResponseDTO findById(long id) {
        log.debug("Fetching reader with ID: {}", id);

        return repository.findById(id)
                .map(ReaderMapper::toReaderResponseDTO)
                .orElseThrow(() -> new ReaderNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReaderResponseDTO> findAll() {
        log.debug("Fetching all readers");

        return repository.findAll().stream()
                .map(ReaderMapper::toReaderResponseDTO)
                .toList();
    }

    @Override
    public ReaderResponseDTO update(long id, ReaderUpdateRequestDTO updateReader) {
        log.info("Updating reader with ID: {}", id);
        var reader = repository.findById(id).orElseThrow(() -> new ReaderNotFoundException(id));

        reader.setFirstName(updateReader.firstName());
        reader.setLastName(updateReader.lastName());
        reader.setEmail(NormalizerEmail.normalize(updateReader.email()));
        reader.setPhone(updateReader.phone());

        repository.save(reader);

        log.info("Reader updated successfully with ID: {}", id);
        return ReaderMapper.toReaderResponseDTO(reader);
    }

    @Override
    public void delete(long id) {
        log.info("Deleting reader with ID: {}", id);

        if (!repository.existsById(id)) throw new ReaderNotFoundException(id);

        repository.deleteById(id);
        log.info("Reader deleted successfully with ID: {}", id);
    }

}
