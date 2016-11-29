package es.udc.pa.pa002.practicapa.web.pages.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.userservice.InstanceAlreadyCreatedException;
import es.udc.pa.pa002.practicapa.model.userservice.InvalidDateException;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.CategoriaEncoder;

/**
 * The Class AnadirEvento.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class AnadirEvento {

/** The categoria select model. */
@Property
private SelectModel categoriaSelectModel;

/** The select model factory. */
@Inject
SelectModelFactory selectModelFactory;

/** The nombre. */
@Property
private String nombre;

/** The categoria. */
@Property
private Categoria categoria;

/** The fecha. */
@Property
private Calendar fecha;

/** The hora. */
@Property
private String hora;

/** The user service. */
@Inject
private UserService userService;

/** The evento. */
private Evento evento;

/** The evento creado. */
@InjectPage
private EventoCreado eventoCreado;

/** The add eventos form. */
@Component
private Form addEventosForm;

/** The fecha date field. */
@Component(id = "fecha")
private DateField fechaDateField;

/** The hora field. */
@Component(id = "hora")
private TextField horaField;

/** The messages. */
@Inject
private Messages messages;

/**
 * Gets the categoria encoder.
 *
 * @return the categoria encoder
 */
public CategoriaEncoder getCategoriaEncoder() {
    return new CategoriaEncoder(userService);
}

/**
 * On activate.
 */
void onActivate() {
    List<Categoria> categorias = userService.findCategories();
    categoriaSelectModel = selectModelFactory.create(categorias, "nombre");
}

/**
 * Validate fecha.
 *
 * @param fecha
 *            the fecha
 * @return true, if successful
 */
boolean validateFecha(String fecha) {
    return Integer.valueOf(fecha.substring(0, 2)) > 23
            || Integer.valueOf(fecha.substring(3)) > 60;
}

/**
 * On validate from add eventos form.
 */
@OnEvent(value = "validate", component = "addEventosForm")
void OnValidateFromAddEventosForm() {
    if (!addEventosForm.isValid()) {
        return;
    }
    if (validateFecha(hora)) {
        addEventosForm
                .recordError(horaField, messages.get("error-invalidTime"));
        return;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    Calendar horaCalendar = Calendar.getInstance();
    try {
        horaCalendar.setTime(sdf.parse(hora));
    } catch (ParseException e1) {
        addEventosForm
                .recordError(horaField, messages.get("error-invalidTime"));
    }
    fecha.set(Calendar.HOUR_OF_DAY, horaCalendar.get(Calendar.HOUR_OF_DAY));
    fecha.set(Calendar.MINUTE, horaCalendar.get(Calendar.MINUTE));
    try {
        evento = userService.addEvento(new Evento(nombre, fecha, categoria));

    } catch (InvalidDateException e) {
        addEventosForm.recordError(fechaDateField,
                messages.get("error-invalidDate"));

    } catch (InstanceAlreadyCreatedException e) {
        addEventosForm.recordError(messages.get("error-eventocreated"));

    }

}

/**
 * On success.
 *
 * @return the object
 */
Object onSuccess() {
    eventoCreado.setIdEvento(evento.getIdEvento());
    return eventoCreado;

}
}
