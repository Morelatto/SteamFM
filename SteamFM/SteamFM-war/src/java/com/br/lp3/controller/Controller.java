package com.br.lp3.controller;

import com.br.lp3.RMI.GeneroJogoManagerLocal;
import com.br.lp3.RMI.JogoManagerLocal;
import com.br.lp3.RMI.LoginManagerLocal;
import com.br.lp3.RMI.RecomendacaoManagerLocal;
import com.br.lp3.RMI.RelacaoManagerLocal;
import com.br.lp3.entities.Album;
import com.br.lp3.entities.Artista;
import com.br.lp3.entities.GeneroJogo;
import com.br.lp3.entities.Jogo;
import com.br.lp3.entities.Musica;
import com.br.lp3.entities.Relacao;
import com.br.lp3.entities.Usuario;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Leandro Meneguzzi - 3144893-3
 * @author Lucas Gianfrancesco - 3147173-0
 * @author Pedro Morelatto - 3142463-5
 */
public class Controller extends HttpServlet {

    @EJB
    private LoginManagerLocal loginManager;
    @EJB
    private GeneroJogoManagerLocal generoJogoManager;
    @EJB
    private JogoManagerLocal jogoManager;
    @EJB
    private RelacaoManagerLocal relacaoManager;
    @EJB
    private RecomendacaoManagerLocal recomendacaoManager;

    private String command;
    private HttpSession session;
    private String steamID;
    private String userSteam;
    private boolean send;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        command = request.getParameter("command");
        session = request.getSession();
        steamID = (String) session.getAttribute("steamID");
        userSteam = (String) session.getAttribute("userSteam");
        send = "-1".equals(steamID) || "".equals(steamID);

        if (command != null) {
            switch (command) {
                case "login":
                    switch (request.getParameter("commandAux")) {
                        case "sistema":
                            Usuario usuario = loginManager.authorize(request.getParameter("loginSistema"), request.getParameter("senhaSistema"));
                            if (usuario != null) {
                                session.setAttribute("user", usuario);
                                if(usuario.getUsuarioSteam()==null){
                                    session.setAttribute("admin", true);
                                }
                                response.sendRedirect("home.jsp");
                            } else {
                                session.setAttribute("usuarioInvalido", "sistema");
                                response.sendRedirect("index.jsp");
                            }
                            break;
                        case "steam":
                            userSteam = request.getParameter("loginSteam");
                            if (!"true".equals(request.getParameter("steamID"))) {
                                steamID = loginManager.getAnonSteamID(userSteam);
                                if (steamID == null) {
                                    session.setAttribute("usuarioInvalido", "steam");
                                    response.sendRedirect("index.jsp");
                                } else {
                                    session.setAttribute("userSteam", userSteam.toUpperCase());
                                    session.setAttribute("steamID", steamID);
                                    response.sendRedirect("home.jsp");
                                }
                            } else {
                                session.setAttribute("steamID", userSteam);
                                response.sendRedirect("home.jsp");
                            }
                            break;
                    }
                    break;
                case "recomendacao":
                    List<GeneroJogo> listaGenerosJogos = generoJogoManager.getListaGenerosByUser(steamID);
                    List<Jogo> listaJogos = jogoManager.getJogosByUser(steamID);
                    List<Relacao> listaRelacao = relacaoManager.getListaRelacao(listaGenerosJogos);
                    for (Iterator<Jogo> it = listaJogos.iterator(); it.hasNext();) {
                        Jogo jogo = it.next();
                        List<GeneroJogo> listaGeneroJogos_JogoObj = generoJogoManager.getListaGenerosByGeneroName(jogo.getListaGeneroJogo());
                        if (listaGeneroJogos_JogoObj.isEmpty()) {
                            it.remove();
                            continue;
                        }
                        List<Musica> listaRelacaoMusica = new ArrayList<>();
                        List<Artista> listaRelacaoArtista = new ArrayList<>();
                        List<Album> listaRelacaoAlbum = new ArrayList<>();
                        listaGeneroJogos_JogoObj.stream().forEach((GeneroJogo generoJogo) -> {
                            listaRelacao.stream().filter((Relacao relacao) -> Objects.equals(relacao.getIdGeneroJogo().getIdGeneroJogo(), generoJogo.getIdGeneroJogo())).forEach((relacao) -> {
                                if (relacao.getIdMusica() != null) {
                                    listaRelacaoMusica.add(relacao.getIdMusica());
                                } else if (relacao.getIdArtista() != null) {
                                    listaRelacaoArtista.add(relacao.getIdArtista());
                                } else {
                                    listaRelacaoAlbum.add(relacao.getIdAlbum());
                                }
                            });
                        });
                        List<Object> recomendacoes = new ArrayList<>();
                        recomendacoes.addAll(recomendacaoManager.getMusicaRecomendacao(listaRelacaoMusica));
                        recomendacoes.addAll(recomendacaoManager.getArtistaRecomendacao(listaRelacaoArtista));
                        recomendacoes.addAll(recomendacaoManager.getAlbumRecomendacao(listaRelacaoAlbum));
                        jogo.setListaRecomendacao(recomendacoes);
                    }
                    session.setAttribute("listaJogos", listaJogos);
                    session.setAttribute("listaRelacao", listaRelacao);
                    response.sendRedirect("recomendacao.jsp");
                    break;
                case "logout":
                    session.invalidate();
                    response.sendRedirect("index.jsp");
                    break;
            }
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
