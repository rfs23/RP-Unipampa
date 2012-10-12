package CN;

public class Parametro {
	
	private String Valor;
	private String Nome;
	
	public Parametro(String nome, String valor) {
		
		this.setNome(nome);
		this.setValor(valor);
		
	}
	
	public void setValor(String valor) {
		Valor = valor;
	}
	public String getValor() {
		return Valor;
	}
	public void setNome(String nome) {
		Nome = nome;
	}
	public String getNome() {
		return Nome;
	}
	

}
