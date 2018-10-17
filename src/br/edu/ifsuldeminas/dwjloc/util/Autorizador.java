package br.edu.ifsuldeminas.dwjloc.util;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.edu.ifsuldeminas.dwjloc.model.Funcionalidade;
import br.edu.ifsuldeminas.dwjloc.model.Usuario;

public class Autorizador implements PhaseListener
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override public void afterPhase(PhaseEvent event)
    {
        FacesContext context = event.getFacesContext();
        String nomePagina = context.getViewRoot().getViewId();

        if (nomePagina.equals("/login.xhtml") || nomePagina.equals("/_template.xhtml"))
        {
            return;
        }

        Usuario logado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");

        if (logado != null)
        {
            Funcionalidade g = (Funcionalidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nomePagina);

            if (g != null)
            {
                return;
            }

            NavigationHandler handler = context.getApplication().getNavigationHandler();
            handler.handleNavigation(context, null, "/_template?faces-redirect=true");
            context.renderResponse();
            return;
        }

        NavigationHandler handler = context.getApplication().getNavigationHandler();
        handler.handleNavigation(context, null, "/login?faces-redirect=true");
        context.renderResponse();
    }

    @Override public void beforePhase(PhaseEvent arg0)
    {

    }

    @Override public PhaseId getPhaseId()
    {
        return PhaseId.RESTORE_VIEW;
    }

}
