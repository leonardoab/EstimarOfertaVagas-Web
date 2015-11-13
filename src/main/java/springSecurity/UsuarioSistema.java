package springSecurity;
import java.util.Collection;

import model.Pessoa;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;



public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;
	
	private Pessoa usuario;
	
	public UsuarioSistema(Pessoa usuario, Collection<? extends GrantedAuthority> authorities) {
		
		super(usuario.getCpf(), "123", authorities);
		this.usuario = usuario;
	}

	public Pessoa getCpf() {
		return usuario;
	}

}
