package br.ufrn.eaj.lojasuplementos.controller;

import br.ufrn.eaj.lojasuplementos.domain.Suplementos;
import br.ufrn.eaj.lojasuplementos.dto.SuplementosForm;
import br.ufrn.eaj.lojasuplementos.repository.SuplementosRepository;
import br.ufrn.eaj.lojasuplementos.service.SuplementosService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
public class SuplementosController {

    // Primeiro injeta o repository
    private final SuplementosRepository repository;
    private final SuplementosService service;

    public SuplementosController(SuplementosRepository repository, SuplementosService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping(value = "/index")
    public String index(Model model){

        // Todos os elementos que não estao marcados como deletados
        List<Suplementos> listaSuplementos = repository.findByIsDeletedIsNull();

        // Envia a lista para o html sob o nome suplementos
        model.addAttribute("suplementos", listaSuplementos);

        return "index";
    }

    @GetMapping(value = "/admin")
    public String admin(Model model){

        // Todos os elementos (deletados e nao deletados)
        List<Suplementos> listaSuplementos = repository.findAll();

        model.addAttribute("suplementos", listaSuplementos);

        return "admin";
    }

    @GetMapping(value = "/cadastro")
    public String cadastro(Model model){

        // Enviando html vazio para o formulario preencher
        model.addAttribute("suplementoForm", new SuplementosForm());

        return "cadastro";
    }

    @GetMapping(value = "/editar")
    public String editar(@RequestParam("id") Long id, Model model) {

        // Busca o suplemento no banco de dados a partir do ID
        Suplementos suplementos = repository.findById(id).get();

        // Transfere os dados da Model (banco) para o DTO (tela)
        SuplementosForm form = new SuplementosForm();
        form.setId(suplementos.getId());
        form.setNome(suplementos.getNome());
        form.setTipo(suplementos.getTipo());
        form.setCodigoSku(suplementos.getCodigoSku());
        form.setMarca(suplementos.getMarca());
        form.setPreco(suplementos.getPreco());

        // Envia o formulário preenchido para a view
        model.addAttribute("suplementoForm", form);

        return "editar";
    }

    @PostMapping(value = "/salvar")
    public String salvar(@Valid @ModelAttribute("suplementoForm") SuplementosForm form, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Adiciona o formulário ao model para a view conseguir exibir os erros
            model.addAttribute("suplementoForm", form);
            
            // Checa se é edição ou cadastro para devolver a tela correta com o erro
            if (form.getId() != null) {
                return "editar";
            }
            return "cadastro";
        }

        Suplementos suplemento;

        if (form.getId() != null) {
            // edição: Busca o item existente no banco
            suplemento = repository.findById(form.getId()).get();
        } else {
            // cadastro: Cria um novo item do zero
            suplemento = new Suplementos();

            // sorteia imagem aleatória no momento do cadastro
            String[] imagens = {"/img/product01.png", "/img/product02.png", "/img/product03.png", "/img/product04.png"};
            String imagemSorteada = imagens[new Random().nextInt(imagens.length)];
            suplemento.setImgUrl(imagemSorteada);
        }

        // Transfere os dados limpos e validados do DTO para a entidade
        suplemento.setNome(form.getNome());
        suplemento.setTipo(form.getTipo());
        suplemento.setCodigoSku(form.getCodigoSku());
        suplemento.setMarca(form.getMarca());
        suplemento.setPreco(form.getPreco());

        // Salva ou atualiza no banco de dados
        repository.save(suplemento);

        // Redireciona para /admin com mensagem de sucesso
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Atualização ocorreu com sucesso.");

        return "redirect:/admin";
    }

    @GetMapping(value = "/detalhe/{id}")
    public String detalhe(@PathVariable("id") Long id, Model model){

        //  Agora o service está buscando
        Suplementos suplemento = service.buscarPorId(id);

        // Se achou, envia para a tela
        model.addAttribute("suplemento", suplemento);

        return "detalhe";
    }

    @GetMapping(value = "/deletar")
    public String deletar(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {

        // Buscando item
        Suplementos suplemento = repository.findById(id).get();

        // Aplica o Soft Delete (Apenas preenche o campo com a data atual)
        suplemento.setIsDeleted(new Date());

        // Atualiza no banco
        repository.save(suplemento);

        // Mensagem exigida na prova
        redirectAttributes.addFlashAttribute("mensagemSucesso", "A remoção ocorreu com sucesso.");

        return "redirect:/index";
    }

    @GetMapping(value = "/restaurar")
    public String restaurar(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {

        // Busca o item no banco
        Suplementos suplemento = repository.findById(id).get();

        // Restaura o item (Adata e volta a ser null)
        suplemento.setIsDeleted(null);

        // Atualiza no banco
        repository.save(suplemento);

        // Mensagem de sucesso
        redirectAttributes.addFlashAttribute("mensagemSucesso", "A restauração ocorreu com sucesso.");

        return "redirect:/index";
    }

    @GetMapping(value = "/adicionarCarrinho")
    public String adicionarCarrinho(@RequestParam("id") Long id, HttpSession session) {

        // Buscando no banco
        Suplementos suplemento = repository.findById(id).get();

        // Tentando recuperar o atributo carrinho da Sessão HTTP
        @SuppressWarnings("unchecked")
        List<Suplementos> carrinho = (List<Suplementos>) session.getAttribute("carrinho");

        // Caso seja nulo é primeiro item adicionado
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }

        // Adiciona no arraylist
        carrinho.add(suplemento);

        // Salva/Atualiza o carrinho na Sessão HTTP
        session.setAttribute("carrinho", carrinho);

        return "redirect:/index";
    }

    @GetMapping(value = "/verCarrinho")
    public String verCarrinho(HttpSession session, RedirectAttributes redirectAttributes, Model model) {

        // Busca a lista do carrinho que está salva na Sessão HTTP
        @SuppressWarnings("unchecked")
        List<Suplementos> carrinho = (List<Suplementos>) session.getAttribute("carrinho");

        // Tratamentos para vazio ou nulo
        if (carrinho == null || carrinho.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não existem itens no carrinho.");
            return "redirect:/index";
        }

        // Se houver itens, adiciona a lista no model para o html conseguir listar
        model.addAttribute("itensCarrinho", carrinho);

        return "carrinho";
    }

    @GetMapping(value = "/finalizarCompra")
    public String finalizarCompra(HttpSession session) {

        // Invalida a Sessão HTTP existente, destruindo o atributo do carrinho e liberando a memória
        // (Estranho comportamento pois quando a sessão é destruída o Spring Security não reconhce mais a sessão e redireciona para a tela de login)
        session.invalidate();

        return "redirect:/index";
    }

}

