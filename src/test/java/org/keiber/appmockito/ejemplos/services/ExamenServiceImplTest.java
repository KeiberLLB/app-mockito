package org.keiber.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keiber.appmockito.ejemplos.models.Examen;
import org.keiber.appmockito.ejemplos.repositories.ExamenRepository;
import org.keiber.appmockito.ejemplos.repositories.PreguntaRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

  @Mock
  ExamenRepository repository;
  @Mock
  PreguntaRepository preguntaRepository;

  @InjectMocks
  ExamenServiceImpl service;

  @BeforeEach
  void setUp() {
    //MockitoAnnotations.openMocks(this);
    //repository = mock(ExamenRepository.class);
    //preguntaRepository = mock(PreguntaRepository.class);
    //service = new ExamenServiceImpl(repository, preguntaRepository);
  }

  @Test
  void findExamenPorNombre() {
    // no se puede simular métodos privados, estáticos o final en Mockito
    when(repository.findAll()).thenReturn(Datos.EXAMENES);
    Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");
    assertTrue(examen.isPresent());
    assertEquals(5L, examen.orElseThrow().getId());
    assertEquals("Matemáticas", examen.get().getNombre());
    // control + shift + Fn + F10 ejecuta el test.
  }

  @Test
  void findExamenPorNombreListaVacia() {
    // no se puede simular métodos privados, estáticos o final en Mockito
    List<Examen> datos = Collections.emptyList();

    when(repository.findAll()).thenReturn(datos);
    Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");
    assertFalse(examen.isPresent());
    // control + shift + Fn + F10 ejecuta el test.
  }

  @Test
  void testPreguntasExamen() {
    when(repository.findAll()).thenReturn(Datos.EXAMENES);
    when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
    Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
    assertEquals(5, examen.getPreguntas().size());
    assertTrue(examen.getPreguntas().contains("aritmética"));
  }

  @Test
  void testPreguntasExamenVerify() {
    when(repository.findAll()).thenReturn(Datos.EXAMENES);
    when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
    Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
    assertEquals(5, examen.getPreguntas().size());
    assertTrue(examen.getPreguntas().contains("aritmética"));
    // verifica que los métodos sean invocados (usados)
    verify(repository).findAll();
    verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
  }

  @Test
  void testNoExisteExamenVerify() {
    when(repository.findAll()).thenReturn(Collections.emptyList());
    when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
    Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
    assertNull(examen);
    // verifica que los métodos sean invocados (usados)
    verify(repository).findAll();
    verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
  }

  @Test
  void testGuardarExamen() {
    Examen newExamen = Datos.EXAMEN;
    newExamen.setPreguntas(Datos.PREGUNTAS);
    when(repository.guardar(any(Examen.class))).thenReturn(Datos.EXAMEN);
    Examen examen = service.guardar(newExamen);
    assertNotNull(examen.getId());
    assertEquals(8L, examen.getId());
    assertEquals("Física", examen.getNombre());

    verify(repository).guardar(any(Examen.class));
    verify(preguntaRepository).guardarVarias(anyList());
  }
}