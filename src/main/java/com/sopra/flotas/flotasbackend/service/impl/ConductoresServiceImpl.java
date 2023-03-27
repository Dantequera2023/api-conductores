package com.sopra.flotas.flotasbackend.service.impl;

import com.sopra.albia.persistence.entity.ConductoresEntity;
import com.sopra.albia.persistence.entity.ConductorespwdsEntity;
import com.sopra.flotas.flotasbackend.contract.generated.models.Conductor;
import com.sopra.flotas.flotasbackend.contract.generated.models.ConductoresDescripciones;
import com.sopra.flotas.flotasbackend.contract.generated.models.ConductoresDescripcionesPage;
import com.sopra.flotas.flotasbackend.contract.generated.models.InlineResponse200;
import com.sopra.flotas.flotasbackend.contract.generated.models.InlineResponse2001;
import com.sopra.flotas.flotasbackend.contract.generated.models.Licencias;
import com.sopra.flotas.flotasbackend.contract.generated.models.UsuarioLogin;
import com.sopra.flotas.flotasbackend.contract.generated.models.UsuarioPassword;
import com.sopra.flotas.flotasbackend.contract.generated.models.Vehiculo;
import com.sopra.flotas.flotasbackend.mapping.ConductoresMapper;
import com.sopra.flotas.flotasbackend.persistence.dao.ConductoresDAO;
import com.sopra.flotas.flotasbackend.persistence.dao.ConductoresPasswordDAO;
import com.sopra.flotas.flotasbackend.persistence.entity.projection.ConductorFilterResultProjection;
import com.sopra.flotas.flotasbackend.security.model.DetallesUsuario;
import com.sopra.flotas.flotasbackend.service.ConductoresService;
import com.sopra.flotas.flotasbackend.service.LicenciasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ConductoresServiceImpl implements ConductoresService {


    private final ConductoresMapper mapper;
    private final LicenciasService licenciasService;
    private final ConductoresDAO conductoresDAO;
    private final ConductoresPasswordDAO conductoresPasswordDAO;
    @Autowired
    private JavaMailSender mailSender;


    @Autowired
    public ConductoresServiceImpl(final ConductoresMapper mapper, final ConductoresDAO conductoresDAO, final LicenciasService licenciasService, ConductoresPasswordDAO conductoresPasswordDAO) {
        this.mapper = mapper;
        this.licenciasService = licenciasService;
        this.conductoresDAO = conductoresDAO;
        this.conductoresPasswordDAO = conductoresPasswordDAO;
    }

    @Override
    @Transactional
    public List<ConductoresDescripcionesPage> obtenerConductores(BigDecimal page) {
        Page<ConductoresEntity> conductores = conductoresDAO.findAll(PageRequest.of(page.intValueExact(), 10));
        List<ConductoresDescripcionesPage> listaCompleta = conductores.stream().map(mapper::mapConductoresToDtoDescripcionesPage).collect(Collectors.toList());
        for (ConductoresDescripcionesPage i : listaCompleta) {
            i.setPaginaInicial(BigDecimal.valueOf(0));
            i.setPaginaFinal(BigDecimal.valueOf(conductores.getTotalPages() - 1L));
            i.setTotalSize(BigDecimal.valueOf(conductores.getTotalElements()));
        }
        return listaCompleta;
    }

    @Override
    @Transactional
    public ConductoresDescripciones obtenerConductorIdConductorYCodCentro(Conductor conductores) {
        Optional<ConductoresEntity> conductoresEntity = conductoresDAO.findById(conductores.getIdConductor().longValueExact());
        if (conductoresEntity.isPresent()) {
            return mapper.mapConductoresToDtoDescripciones(conductoresEntity.get());
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public List<ConductoresDescripcionesPage> obtenerConductoresPorIDMasDatos(Conductor conductor, List<String> listCodCentro, List<String> listCodTerritorial,
                                                                              List<BigDecimal> listTipPersona, String codLicencia, BigDecimal page) {
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        ConductoresEntity conductorEntity = mapper.mapConductoresToEntityFilter2(conductor);
        Page<ConductorFilterResultProjection> listaconductores = conductoresDAO.findConductoresByCentroTerriPersoLiceIdConductor(conductorEntity, listCodCentro, listCodTerritorial, listTipPersona, codLicencia,
                authenticationToken.getAuthorities().stream().findFirst().get().getAuthority(),((DetallesUsuario)authenticationToken.getPrincipal()).getIdConductor(),page);
        List<ConductoresDescripcionesPage> listaCompleta = listaconductores.stream().map(mapper::mapConductoresProjectionToDtoDescripcionesPage).collect(Collectors.toList());
        for (ConductoresDescripcionesPage i : listaCompleta) {
            i.setPaginaInicial(BigDecimal.valueOf(0));
            i.setPaginaFinal(BigDecimal.valueOf(listaconductores.getTotalPages() - 1L));
            i.setTotalSize(BigDecimal.valueOf(listaconductores.getTotalElements()));
        }
        return listaCompleta;
    }

    @Override
    @Transactional
    public InlineResponse200 resetPassword(UsuarioPassword usuarioPassword) {
        InlineResponse200 response = new InlineResponse200();
        ConductoresEntity temp = conductoresDAO.obtenerConductorPorCorreo(usuarioPassword.getEmail());
        if (temp != null) {
            ConductorespwdsEntity nuevo = new ConductorespwdsEntity();
            nuevo.setIdConductor(temp.getIdConductor());
            nuevo.setConductoresByIdConductor(temp);
            nuevo.setPwd(usuarioPassword.getContrasena());
            ConductorespwdsEntity tst = conductoresPasswordDAO.save(nuevo);
            if (Objects.equals(tst.getPwd(), usuarioPassword.getContrasena())) {
                response.setEmail(nuevo.getConductoresByIdConductor().getCorreoElectronico());
                response.setMensaje(true);
            } else {
                response.setEmail(nuevo.getConductoresByIdConductor().getCorreoElectronico());
                response.setMensaje(false);
            }
        } else {
            response.setEmail(usuarioPassword.getEmail());
            response.setMensaje(false);
        }
        return response;
    }

    @Override
    public InlineResponse2001 sendMail(String email, String app) {
        try {
            if (conductoresDAO.obtenerConductorPorCorreo(email) != null) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(email);
                helper.setSubject("subjet");
                if (Objects.equals(app, "backoffice")) {
                    helper.setText("http://localhost:4200/#/change-password/"+ email + "\n\n\n" + "messaje");
                }else if (Objects.equals(app, "movil")){
                    helper.setText("http://localhost:4200/#/aplicacion-movil" + "\n\n\n" + "messaje");
                }
                mailSender.send(message);
                return new InlineResponse2001(email, true, "Correcto, el envio a funcionado");
            } else {
                return new InlineResponse2001(email, false, "Error");
            }

        } catch (MailSendException me) {
            log.error("failed to send email");
            log.error("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB --->  " + me.getMostSpecificCause());
            return new InlineResponse2001(email, false, me.getMessage());
        } catch (MessagingException e) {
            log.error("failed to send email" + e.getNextException().getMessage());
            return new InlineResponse2001(email, false, e.getNextException().getMessage());
        }
    }

    @Override
    @Transactional
    public List<ConductoresDescripciones> insertarConductores(ConductoresDescripciones conductores) {
        List<ConductoresDescripciones> listaConductores = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        conductores.setDtFechaultimamodificacion(formatter.format(LocalDateTime.now()));
        ConductoresEntity conductor = conductoresDAO.save(mapper.mapConductoresDescripcionesToConductoresEntity(conductores));
        listaConductores.add(obtenerConductorIdConductorYCodCentro(Conductor.builder().idConductor(BigDecimal.valueOf(conductor.getIdConductor())).build()));
        return listaConductores;
    }

    @Override
    @Transactional
    public List<Licencias> obtenerLincenciasConductor(BigDecimal idConductor) {
        return licenciasService.obtenerLicenciasPorIDMasDatos(Licencias.builder().idConductor(idConductor).build());
    }

    @Override
    @Transactional
    public List<Vehiculo> obtenerVehiculosConductor(BigDecimal idConductor) {
        Optional<ConductoresEntity> conductor = conductoresDAO.findById(idConductor.longValueExact());
        List<Vehiculo> vehiculos = null;
        if (conductor.isPresent()) {
            vehiculos = conductor.get().getConductorespropiedadvehiculosByIdConductor().stream().map(
                    t -> Vehiculo.builder().idVehiculo(t.getVehiculosByIdVehiculo().getIdVehiculo()).sMatricula(t.getVehiculosByIdVehiculo().getsMatricula()).build()).collect(Collectors.toList());
        }
        return vehiculos;
    }

    @Override
    @Transactional
    public Conductor obtenerConductorCodUsuarioMailPassword(UsuarioLogin usuarioLogin) {
        ConductoresEntity conductor = conductoresDAO.obtenerConductorPorCodEmpleadoOCorreoYPassword(usuarioLogin.getUsuario(), usuarioLogin.getContrasena());
        if (conductor == null) {
            return null;
        } else {
            return mapper.mapConductoresEntityToDto(conductor);
        }
    }
}
