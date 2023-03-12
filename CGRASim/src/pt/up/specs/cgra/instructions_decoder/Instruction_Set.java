package pt.up.specs.cgra.instructions_decoder;

public enum Instruction_Set {
	
	SET,			//define funcionalidade de um PE
	SET_IO,			//define entradas e saidas de um PE
	INFO,			//obter info do CGRA - tipo de hierarquia/mesh e tamanho, ocupa�ao de PEs, n de threads/kernels, contexto atual, PEs livres, tipo de PEs
	PE_INFO,		//obter info de um PE
	MEM_INFO,		//obter info da memoria
	STORE_CTX,		//armazenar um contexto
	SWITCH_CTX,		//mudar para outro contexto
	RUN,			//executar ou parar - tudo/s� um thread x/s� n ciclos/at� resultado ter nivel de precisao x
	PAUSE,			//pausar execu��o
	RESET			//reset a tudo/s� um thread x/s� �s memorias e contadores, mantendo contexto
	
	

}
