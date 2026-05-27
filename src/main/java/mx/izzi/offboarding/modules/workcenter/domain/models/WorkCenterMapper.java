package mx.izzi.offboarding.modules.workcenter.domain.models;

import mx.izzi.offboarding.modules.workcenter.domain.dtos.WorkCenterDto;
import mx.izzi.offboarding.modules.workcenter.domain.entities.WorkCenterEntity;

public class WorkCenterMapper {
    public static WorkCenterEntity toEntity(WorkCenter workCenter) {
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
                .workCenterId(workCenter.getWorkCenterId())
                .workCenter(workCenter.getWorkCenter())
                .departmentId(workCenter.getDepartmentId())
                .department(workCenter.getDepartment())
                .dependents(workCenter.getDependents())
                .isActive(workCenter.getIsActive())
                .createdAt(workCenter.getCreatedAt())
                .updatedAt(workCenter.getUpdatedAt())
                .createdBy(workCenter.getCreatedBy())
                .updatedBy(workCenter.getUpdatedBy())
                .build();
    }

    public static WorkCenterDto from(WorkCenter workCenter) {
        return WorkCenterDto.builder()
                .region(workCenter.getRegion())
                .locality(workCenter.getLocality())
                .business(workCenter.getBusiness())
                .division(workCenter.getDivision())
                .company(workCenter.getCompany())
                .workCenter(workCenter.getWorkCenter())
                .department(workCenter.getDepartment())
                .dependents(workCenter.getDependents())
                .build();
    }
}
