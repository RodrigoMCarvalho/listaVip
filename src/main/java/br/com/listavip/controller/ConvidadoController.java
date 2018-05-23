package br.com.listavip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.enviadorEmail.EmailService;
import br.com.listavip.model.Convidado;
import br.com.listavip.repository.ConvidadoRepository;

@Controller
public class ConvidadoController {

	@Autowired
	ConvidadoRepository repository;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/listaconvidados")
	public String listaConvidados(Model model) {

		Iterable<Convidado> convidados = repository.findAll(); // obt�m os dados do BD

		model.addAttribute("convidados", convidados); // No Spring MVC, o envio de vari�veis para a view
														// � realizada pelo Model - OBS: "convidados" � referente a view
		return "listaconvidados";
	}

	@RequestMapping(value = "salvar", method = RequestMethod.POST)
	public String salvar(@RequestParam("nome") String nome, @RequestParam("email") String email,
			@RequestParam("telefone") String telefone) {
		
		Convidado convidado = new Convidado(nome, email, telefone);
		repository.save(convidado);
		
		new EmailService().enviar(nome, email); //Jar criado para o envio de email (enviadorEmail)

		return "redirect:/listaconvidados"; //ap�s persistir os dados, redireciona para a p�gina
	}
	
	@RequestMapping("/delete{id}")
	public String excluir(@PathVariable Long id) {
		repository.deleteById(id);
		
		return "redirect:/listaconvidados";
	}

}