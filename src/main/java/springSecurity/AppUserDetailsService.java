package springSecurity;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.Pessoa;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;






public class AppUserDetailsService implements UserDetailsService {



	public UserDetails loadUserByUsername(String UsuarioRede)throws UsernameNotFoundException {
		Pessoa usuario = new  Pessoa();

		usuario.setCpf(UsuarioRede);



		UsuarioSistema user = null;

		if (usuario != null) {
			user = new UsuarioSistema(usuario, getGrupos(usuario));
		}

		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Pessoa usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();




		/*for (GrupoMatricula grupoMatricula : gruposMatricula) {
			gruposPermissao = grupoPermissaoDAO.buscarPorGrupo(grupoMatricula.getGrupo().getId());
		}*/


		if (usuario.getCpf().toUpperCase().equals("ADMIN") ){
			
			System.out.println(usuario.getCpf().toUpperCase());

			authorities.add(new SimpleGrantedAuthority("admin"));

		}
		else{

		
			authorities.add(new SimpleGrantedAuthority("comum"));
			}
		
		

			

		return authorities;

	}	

}
