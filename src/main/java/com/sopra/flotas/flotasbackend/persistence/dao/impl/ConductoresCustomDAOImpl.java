package com.sopra.flotas.flotasbackend.persistence.dao.impl;

import com.sopra.albia.persistence.entity.CentrosEntity;
import com.sopra.albia.persistence.entity.ConductoresEntity;
import com.sopra.albia.persistence.entity.ConductorescentrosEntity;
import com.sopra.albia.persistence.entity.ConductorespwdsEntity;
import com.sopra.albia.persistence.entity.LicenciasconductorEntity;
import com.sopra.albia.persistence.entity.TerritorialesEntity;
import com.sopra.albia.persistence.entity.TipospersonasEntity;
import com.sopra.flotas.flotasbackend.persistence.dao.ConductoresCustomDAO;
import com.sopra.flotas.flotasbackend.persistence.entity.projection.ConductorFilterResultProjection;
import com.sopra.albia.persistence.enums.Rol;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConductoresCustomDAOImpl implements ConductoresCustomDAO {


    public static final String COD_CENTRO = "codCentro";
    @Autowired
    private EntityManager em;

    private Join<ConductorescentrosEntity, CentrosEntity> centrosJoin;

    private Join<ConductoresEntity, TipospersonasEntity> tipoPersonas;

    private Join<ConductoresEntity, TerritorialesEntity> territoriales;

    private Join<ConductoresEntity, LicenciasconductorEntity> licencias;


    public Page<ConductorFilterResultProjection> findConductoresByCentroTerriPersoLiceIdConductor(ConductoresEntity conductor, List<String> listCodCentro, List<String> listCodTerritorial,
                                                                                                  List<BigDecimal> listTipPersona, String codLicencia, String rol, Long idUsuario, BigDecimal page) {
        CriteriaBuilder cb = em.getCriteriaBuilder();


        CriteriaQuery<ConductorFilterResultProjection> cq = cb.createQuery(ConductorFilterResultProjection.class);
        CriteriaQuery<Long> cqCount = cb.createQuery(Long.class);

        Root<ConductoresEntity> root = cq.from(ConductoresEntity.class);
        Root<ConductoresEntity> rootCount = cqCount.from(ConductoresEntity.class);

        createJoins(rootCount);
        cqCount.select(cb.countDistinct(rootCount)).where(cb.and(this.createPredicatesByCentroTerriPersoLiceIdConductor(cqCount,cb, rootCount, conductor, listCodCentro, listCodTerritorial, listTipPersona, codLicencia,rol,idUsuario).toArray(new Predicate[0])));
        Long count = em.createQuery(cqCount).getSingleResult();
        createJoins(root);
        cq.multiselect(root, centrosJoin, territoriales)
                .where(cb.and(this.createPredicatesByCentroTerriPersoLiceIdConductor(cq,cb, root, conductor, listCodCentro, listCodTerritorial, listTipPersona, codLicencia,rol,idUsuario).toArray(new Predicate[0]))).groupBy(root,centrosJoin, territoriales);
        TypedQuery<ConductorFilterResultProjection> query = em.createQuery(cq).setMaxResults(10).setFirstResult(10*(page.intValueExact()));
        return new PageImpl<>(query.getResultList(), PageRequest.of(page.intValueExact(), 10), count);
    }

    private void createJoins(Root<ConductoresEntity> root) {
        tipoPersonas = root.join("tipospersonasByIdTipopersona");
        licencias = root.join("licenciasconductorsByIdConductor", JoinType.LEFT);
        Join<ConductoresEntity, ConductorescentrosEntity> conductorescentrosEntityJoin = root.join("conductorescentrosByIdConductor");
        centrosJoin = conductorescentrosEntityJoin.join("centrosByCodCentro");
        territoriales = centrosJoin.join("territorialesByCodTerritorial");
    }

    private List<Predicate> createPredicatesByCentroTerriPersoLiceIdConductor(CriteriaQuery<?> cq, CriteriaBuilder cb, Root<ConductoresEntity> root, ConductoresEntity conductor, List<String> listCodCentro, List<String> listCodTerritorial,
                                                                              List<BigDecimal> listTipPersona, String codLicencia, String rol, Long idUsuario) {
        List<Predicate> listPredicates = new ArrayList<>();
        Predicate starPredicate = cb.equal(cb.literal(1), 1);
        listPredicates.add(starPredicate);
        if (listCodCentro != null) {
            listPredicates.add(centrosJoin.get(COD_CENTRO).in(listCodCentro));
        }
        if (listTipPersona != null) {
            listPredicates.add(tipoPersonas.get("idTipopersona").in(listTipPersona));
        }
        if (listCodTerritorial != null) {
            listPredicates.add(territoriales.get("codTerritorial").in(listCodTerritorial));
        }
        if (StringUtils.isNoneBlank(conductor.getStApellidos())) {
            listPredicates.add(cb.equal(root.get("stApellidos"), conductor.getStApellidos()));
        }

        if (StringUtils.isNoneBlank(conductor.getStNombre())) {
            listPredicates.add(cb.equal(root.get("stNombre"), conductor.getStNombre()));
        }

        if (StringUtils.isNoneBlank(codLicencia)) {
            listPredicates.add(cb.equal(licencias.get("codLicencia"), codLicencia));
        }

        if(!(Rol.ADMIN.name().equals(rol) || Rol.VISOR.name().equals(rol))){
            listPredicates.add(centrosJoin.get(COD_CENTRO).in(subqueryCentroUsuario(cq,cb,idUsuario)));
        }

        return listPredicates;
    }

    public ConductoresEntity obtenerConductorPorCodEmpleadoOCorreoYPassword(String usuario, String password){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConductoresEntity> cq = cb.createQuery(ConductoresEntity.class);
        Root<ConductoresEntity> root = cq.from(ConductoresEntity.class);
        Join<ConductoresEntity, ConductorespwdsEntity>  joinPassword = root.join("conductorespwdsByIdConductor");
        List<Predicate> listPredicates = new ArrayList<>();
        listPredicates.add(cb.or(cb.equal(root.get("codEmpleado"),usuario),cb.equal(root.get("correoElectronico"),usuario)));
        listPredicates.add(cb.equal(joinPassword.get("pwd"),password));
        cq.select(root).where(cb.and(listPredicates.toArray(new Predicate[0])));
        List<ConductoresEntity> list = em.createQuery(cq).getResultList();
        return list.isEmpty() ? null: list.get(0);
    }

    @Override
    public ConductoresEntity obtenerConductorPorCorreo(String mail) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConductoresEntity> cq = cb.createQuery(ConductoresEntity.class);
        Root<ConductoresEntity> root = cq.from(ConductoresEntity.class);
        cq.select(root).where(cb.equal(root.get("correoElectronico"),mail));
        List<ConductoresEntity> list = em.createQuery(cq).getResultList();
        return list.isEmpty() ? null: list.get(0);
    }

    private Subquery<String> subqueryCentroUsuario(CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb, Long idConductor){
        Subquery<String> existeQuery = criteriaQuery.subquery(String.class);
        Root<CentrosEntity> root = existeQuery.from(CentrosEntity.class);
        Join<CentrosEntity, ConductorescentrosEntity> conductorCentroJoin = root.join("conductorescentrosByCodCentro");
        Join<ConductorescentrosEntity, ConductoresEntity> conductorJoin = conductorCentroJoin.join("conductoresByIdConductor");
        existeQuery.select(root.get(COD_CENTRO)).where(cb.equal(conductorJoin.get("idConductor"),idConductor));
        return existeQuery;
    }


}
