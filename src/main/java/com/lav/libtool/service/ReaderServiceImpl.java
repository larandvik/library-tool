package com.lav.libtool.service;

import com.lav.libtool.dto.reader.ReaderCreateRequestDTO;
import com.lav.libtool.dto.reader.ReaderResponseDTO;
import com.lav.libtool.dto.reader.ReaderUpdateRequestDTO;
import com.lav.libtool.entity.Reader;
import com.lav.libtool.exceptions.BookErrorType;
import com.lav.libtool.exceptions.BookException;
import com.lav.libtool.mappers.ReaderMapper;
import com.lav.libtool.repository.ReaderRepository;
import com.lav.libtool.util.NormalizerEmail;
import com.lav.libtool.util.NormalizerPhone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
        log.info("Creating new reader");
        var normEmail = NormalizerEmail.normalize(newReader.email());
        var normPhone = NormalizerPhone.normalize(newReader.phone());

        Reader reader = new Reader(
                newReader.firstName(),
                newReader.lastName(),
                normEmail,
                normPhone);

        try {
            var savedReader = repository.save(reader);
            log.info("Reader created successfully with ID: {}", savedReader.getId());
            return ReaderMapper.toReaderResponseDTO(savedReader);
        } catch (DataIntegrityViolationException ex) {
            throw new BookException(BookErrorType.READER_ALREADY_EXISTS);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReaderResponseDTO findById(long id) {
        log.debug("Fetching reader with ID: {}", id);

        return ReaderMapper.toReaderResponseDTO(getReaderOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Reader findEntityById(long id) {
        log.debug("Fetching reader with ID: {}", id);

        return getReaderOrThrow(id);
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
        var normEmail = NormalizerEmail.normalize(updateReader.email());
        var normPhone = NormalizerPhone.normalize(updateReader.phone());
        var reader = getReaderOrThrow(id);

        reader.setFirstName(updateReader.firstName());
        reader.setLastName(updateReader.lastName());
        reader.setEmail(normEmail);
        reader.setPhone(normPhone);

        try {
            var updated = repository.saveAndFlush(reader);
            log.info("Reader updated successfully with ID={}", id);
            return ReaderMapper.toReaderResponseDTO(updated);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Duplicate email on update: {}", updateReader.email());
            throw new BookException(BookErrorType.READER_ALREADY_EXISTS);
        }
    }

    @Override
    public void delete(long id) {
        log.info("Deleting reader with ID: {}", id);

        var reader = getReaderOrThrow(id);
        repository.delete(reader);

        log.info("Reader deleted successfully with ID: {}", id);
    }

    private Reader getReaderOrThrow(long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("READER_NOT_FOUND: id={}", id);
                    return new BookException(BookErrorType.READER_NOT_FOUND);
                });
    }

}
