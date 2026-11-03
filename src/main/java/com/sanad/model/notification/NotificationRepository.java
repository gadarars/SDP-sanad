package com.sanad.model.notification;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;

/** MODEL: database access for sent notifications. */
@RegisterConstructorMapper(Notification.class)
public interface NotificationRepository {

    @SqlUpdate("INSERT INTO notifications(recipient, channel, address, message) VALUES(:recipient, :channel, :address, :message)")
    void add(@BindBean Notification n);

    @SqlQuery("SELECT * FROM notifications ORDER BY sent_at DESC, id DESC")
    List<Notification> findAll();
}
