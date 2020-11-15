package no.fint.portal.audit;

import lombok.Builder;
import lombok.Data;
import no.fint.portal.model.contact.Contact;
import no.fint.portal.model.organisation.Organisation;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;

@Data
@Builder
@Document
public class UserEvent {

    @Id
    private String id;

    @Version
    private long version;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @CreatedDate
    private LocalDateTime createdDate;

    private UserEventOperation operation;
    private Object object;
    private Contact user;
    private Organisation organisation;
    private String type;
}
