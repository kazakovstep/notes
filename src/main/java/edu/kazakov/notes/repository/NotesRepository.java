package edu.kazakov.notes.repository;

import edu.kazakov.notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface NotesRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(Long id);
}
