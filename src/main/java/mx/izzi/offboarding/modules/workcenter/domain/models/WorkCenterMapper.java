package mx.izzi.offboarding.modules.workcenter.domain.models;

import mx.izzi.offboarding.modules.workcenter.domain.entities.WorkCenterEntity;

public class WorkCenterMapper {
    public static mx.izzi.offboarding.modules.workcenter.domain.entities.WorkCenterEntity toEntity(WorkCenter workCenter) {
        return WorkCenterEntity.builder()
                .id(workCenter.getId())
                .regionId(workCenter.getRegionId())
                .region(workCenter.getRegion())
                .localityId(workCenter.getLocalityId())
                .locality(workCenter.getLocality())
                .businessId(workCenter.getBusinessId())
                .business(workCenter.getBusiness())
                .divisionId(workCenter.getDivisionId())
                .division(workCenter.getDivision())
                .companyId(workCenter.getCompanyId())
                .company(workCenter.getCompany())
                .workCenterId(workCenter.getId())
                .workCenter(workCenter.getWorkCenter())
                .departmentId(workCenter.getDepartmentId())
                .department(workCenter.getDepartment())
                .department(workCenter.getDepartment())
                .isActive(workCenter.getIsActive())
                .createdAt(workCenter.getCreatedAt())
                .updatedAt(workCenter.getUpdatedAt())
                .createdBy(workCenter.getCreatedBy())
                .updatedBy(workCenter.getUpdatedBy())
                .build();
    }
}
