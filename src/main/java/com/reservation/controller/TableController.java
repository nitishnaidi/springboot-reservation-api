package com.reservation.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reservation.exception.ResourceNotFoundException;
import com.reservation.model.Table;
import com.reservation.repository.TableRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin({"http://localhost:4200"})
public class TableController {

  @Autowired
  TableRepository tableRepository;

  @GetMapping("/tables")
  public ResponseEntity<List<Table>> getAllTables(@RequestParam(required = false) String title) {
    List<Table> tables = new ArrayList<Table>();

    if (title == null)
      tableRepository.findAll().forEach(tables::add);
//    else
//      tableRepository.findByTitleContaining(title).forEach(tables::add);

    if (tables.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(tables, HttpStatus.OK);
  }

  @GetMapping("/tables/{id}")
  public ResponseEntity<Table> getTableById(@PathVariable("id") long id) {
    Table table = tableRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Table with id = " + id));

    return new ResponseEntity<>(table, HttpStatus.OK);
  }

  @PostMapping("/tables")
  public ResponseEntity<Table> createTable(@RequestBody Table table) {
    Table _table = tableRepository.save(new Table(table.getGuestCapacity(), table.getDescription()));
    return new ResponseEntity<>(_table, HttpStatus.CREATED);
  }

  @PutMapping("/tables/{id}")
  public ResponseEntity<Table> updateTable(@PathVariable("id") long id, @RequestBody Table table) {
    Table _table = tableRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Table with id = " + id));

    _table.setDescription(table.getDescription());
    _table.setGuestCapacity(table.getGuestCapacity());
    
    return new ResponseEntity<>(tableRepository.save(_table), HttpStatus.OK);
  }

  @DeleteMapping("/tables/{id}")
  public ResponseEntity<HttpStatus> deleteTable(@PathVariable("id") long id) {
    tableRepository.deleteById(id);
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/tables")
  public ResponseEntity<HttpStatus> deleteAllTables() {
    tableRepository.deleteAll();
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

//  @GetMapping("/tables/published")
//  public ResponseEntity<List<Table>> findByPublished() {
//    List<Table> tables = tableRepository.findByPublished(true);
//
//    if (tables.isEmpty()) {
//      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//    
//    return new ResponseEntity<>(tables, HttpStatus.OK);
//  }
}
