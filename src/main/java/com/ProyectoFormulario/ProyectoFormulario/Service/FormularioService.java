package com.ProyectoFormulario.ProyectoFormulario.Service;


import com.ProyectoFormulario.ProyectoFormulario.Entity.*;
import com.ProyectoFormulario.ProyectoFormulario.Enum.EstadoFormulario;
import com.ProyectoFormulario.ProyectoFormulario.Enum.EstadoRevision;
import com.ProyectoFormulario.ProyectoFormulario.Enum.TipoRol;
import com.ProyectoFormulario.ProyectoFormulario.IRepository.*;
import com.ProyectoFormulario.ProyectoFormulario.IService.IFormularioService;
import com.ProyectoFormulario.ProyectoFormulario.exceptions.PersonaNoAsociadaException;
import com.ProyectoFormulario.ProyectoFormulario.exceptions.RolNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;



@Service
public class FormularioService extends ABaseService<Formulario> implements IFormularioService {

    @Override
    protected IBaseRepository<Formulario, Long> getRepository() {
        return formularioRepository;
    }

    @Autowired
    private IFormularioRepository formularioRepository;

    @Autowired
    private IAsignaturaDocenciaRepository asignaturaDocenciaRepository;

    @Autowired
    private IActividadesAdministrativaRepository actividadesAdministrativaRepository;

    @Autowired
    private IActividadesCientificasRepository actividadesCientificasRepository;

    @Autowired
    private IActividadesDocenciaRepository actividadesDocenciaRepository;

    @Autowired
    private IActividadesLaboresRepository actividadesLaboralesRepository;

    @Autowired
    private IRevisionFormularioRepository revisionFormularioRepository;



    @Override
    public Formulario crearFormulario(Formulario formulario, Usuario usuario) throws Exception {
        // Obtener el rol de docente del usuario
        Rol rolDocente = usuario.getRoles().stream()
                .filter(rol -> rol.getTipoRol() == TipoRol.DOCENTE)
                .findFirst()
                .orElseThrow(() -> new RolNoEncontradoException("Rol de docente no encontrado"));

        // Obtener la persona que está creando el formulario
        Persona personaCreando = usuario.getPersona();
        if (personaCreando == null) {
            throw new PersonaNoAsociadaException("El usuario no tiene asociada una persona.");
        }

        // Establecer el nombre completo del profesor
        String nombreCompletoProfesor = personaCreando.getNombre() + " " + personaCreando.getApellido();
        System.out.println("Formulario creado por: " + nombreCompletoProfesor);
        formulario.setNombreProfesor(nombreCompletoProfesor); // Establecer el nombre completo

        // Verificar que el usuario tenga el rol correcto
        verificarRol(usuario, TipoRol.DOCENTE);
        formulario.setRol(rolDocente); // Establecer el rol en el formulario

        // Guardar el formulario usando el método save de la clase base
        return save(formulario); // save() proviene de ABaseService
    }


    @Override
    public AsignaturaDocencia agregarAsignaturaDocencia(Long id, AsignaturaDocencia asignaturaDocencia) throws Exception {
        Formulario formulario = findById(id);
        asignaturaDocencia.setFormulario(formulario);
        return asignaturaDocenciaRepository.save(asignaturaDocencia);
    }

    @Override
    public ActividadesAdministrativa agregarActividadesAdministrativa(Long id, ActividadesAdministrativa actividadesAdministrativa) throws Exception {
        Formulario formulario = findById(id);
        actividadesAdministrativa.setFormulario(formulario);
        return actividadesAdministrativaRepository.save(actividadesAdministrativa);
    }

    @Override
    public ActividadesCientificas agregarActividadesCientificas(Long id, ActividadesCientificas actividadesCientificas) throws Exception {
        Formulario formulario = findById(id);
        actividadesCientificas.setFormulario(formulario);
        return actividadesCientificasRepository.save(actividadesCientificas);
    }

    @Override
    public ActividadesDocencia agregarActividadesDocencia(Long id, ActividadesDocencia actividadesDocencia) throws Exception {
        Formulario formulario = findById(id);
        actividadesDocencia.setFormulario(formulario);
        return actividadesDocenciaRepository.save(actividadesDocencia);
    }

    @Override
    public ActividadesLabores agregarActividadesLabores(Long id, ActividadesLabores actividadesLabores) throws Exception {
        Formulario formulario = findById(id);
        actividadesLabores.setFormulario(formulario);
        return actividadesLaboralesRepository.save(actividadesLabores);
    }


    @Override
    public void revisarFormulario(Formulario formulario, Usuario usuario, String comentario, EstadoRevision estadoRevision) throws Exception {
        // Verificamos el rol directamente

        TipoRol tipoRol = usuario.getRoles().stream()
                .map(Rol::getTipoRol)
                .findFirst()
                .orElseThrow(() -> new Exception("El usuario no tiene un rol válido para realizar esta acción."));

        switch (tipoRol) {
            case DIRECCIONPROGRAMA:
                if (formulario.getEstado() == EstadoFormulario.DILIGENCIADO) {
                    if (estadoRevision == EstadoRevision.RECHAZADO) {
                        formulario.setEstado(EstadoFormulario.DILIGENCIADO); // Devuelve al estado original
                        guardarRevision(formulario, usuario, comentario, EstadoRevision.RECHAZADO);
                    } else if (estadoRevision == EstadoRevision.PENDIENTE) {
                        // Si se deja en pendiente, no cambiamos el estado del formulario
                        guardarRevision(formulario, usuario, comentario, EstadoRevision.PENDIENTE);
                        return; // Salimos del método para no hacer nada más
                    } else {
                        formulario.setEstado(EstadoFormulario.APROBADO_Y_FIRMADO); // Aprobado
                        guardarRevision(formulario, usuario, comentario, EstadoRevision.APROBADO);
                    }
                } else {
                    throw new Exception("El formulario no está en un estado válido para ser revisado por el Decano.");
                }
                break;

            case DECANO, VICERRECTORIA:
                if (formulario.getEstado() == EstadoFormulario.REVISADO) {
                    if (estadoRevision == EstadoRevision.RECHAZADO) {
                        formulario.setEstado(EstadoFormulario.REVISADO); // Devuelve al estado original
                        guardarRevision(formulario, usuario, comentario, EstadoRevision.RECHAZADO);
                    } else if (estadoRevision == EstadoRevision.PENDIENTE) {
                        // Si se deja en pendiente, no cambiamos el estado del formulario
                        guardarRevision(formulario, usuario, comentario, EstadoRevision.PENDIENTE);
                        return; // Salimos del método para no hacer nada más
                    } else {
                        formulario.setEstado(EstadoFormulario.APROBADO_Y_FIRMADO); // Aprobado
                        guardarRevision(formulario, usuario, comentario, EstadoRevision.APROBADO);
                    }
                } else {
                    throw new Exception("El formulario no está en un estado válido para ser revisado por el Decano.");
                }
                break;

            default:
                throw new Exception("El formulario no está en un estado válido para ser procesado.");
        }

        // Guardamos los cambios en el formulario
        save(formulario);
    }

    private void guardarRevision(Formulario formulario, Usuario usuario, String comentario, EstadoRevision estadoRevision) {
        RevisionFormulario revision = new RevisionFormulario();
        revision.setFormulario(formulario);
        revision.setRolRevisor((Rol) usuario.getRoles());
        revision.setFechaRevision(LocalDateTime.now());
        revision.setComentarioRevision(comentario);
        revision.setEstadoRevision(estadoRevision);

        // Guardar la revisión en la base de datos
        revisionFormularioRepository.save(revision);
    }

    private void verificarRol(Usuario usuario, TipoRol tipoRol) throws Exception {
        boolean tieneRol = usuario.getRoles().stream()
                .anyMatch(rol -> rol.getTipoRol() == tipoRol);
        if (!tieneRol) {
            throw new Exception("Permiso denegado: El usuario no tiene el rol necesario.");
        }
    }

    public int calcularTotalHoras(Formulario formulario) {
        int totalHoras = 0;

        // Sumar horas de AsignaturaDocencia
        if (formulario.getAsignaturaDocencia() != null) {
            totalHoras += (formulario.getAsignaturaDocencia().getHorasSemanales());
        }

        // Sumar horas de ActividadesAdministrativa
        if (formulario.getActividadesAdministrativa() != null) {
            totalHoras += (formulario.getActividadesAdministrativa().getHorasSemanales());
        }

        // Sumar horas de ActividadesCientificas
        if (formulario.getActividadesCientificas() != null) {
            totalHoras += (formulario.getActividadesCientificas().getHorasSemanales());
        }

        // Sumar horas de ActividadesLabores
        if (formulario.getActividadesLabores() != null) {
            totalHoras += (formulario.getActividadesLabores().getHorasSemanales());
        }

        // Sumar horas de ActividadesDocencia
        if (formulario.getActividadesDocencia() != null) {
            totalHoras += (formulario.getActividadesDocencia().getHorasSemanales());
        }

        // Sumar horas de otras actividades, según sea necesario

        return totalHoras;

        }





}





