document.addEventListener("DOMContentLoaded", function () {
    const filtroNome = document.getElementById("filtroNome");
    const filtroCPF = document.getElementById("filtroCPF");
    const filtroPerfil = document.getElementById("filtroPerfil");
    const filtroStatus = document.getElementById("filtroStatus");
    const tabelaUsuarios = document.getElementById("tabelaUsuarios");
    const linhasTabela = tabelaUsuarios.getElementsByTagName("tr");
    const limparFiltros = document.getElementById("limparFiltros");

    function filtrarUsuarios() {
        const nomeValor = filtroNome.value.trim().toLowerCase();
        const cpfValor = filtroCPF.value.trim().toLowerCase();
        const perfilValor = filtroPerfil.value.trim().toLowerCase();
        const statusValor = filtroStatus.value.trim().toLowerCase();

        for (let i = 0; i < linhasTabela.length; i++) {
            const linha = linhasTabela[i];

            const nome = linha.cells[1].innerText.trim().toLowerCase();
            const cpf = linha.cells[3].innerText.trim().toLowerCase();
            const perfil = linha.cells[4].innerText.trim().toLowerCase();
            const status = linha.cells[5].innerText.trim().toLowerCase();

            const correspondeNome = nome.includes(nomeValor);
            const correspondeCPF = cpf.includes(cpfValor);
            const correspondePerfil = perfilValor === "" || perfil.includes(perfilValor);
            const correspondeStatus = statusValor === "" || status.includes(statusValor);

            if (correspondeNome && correspondeCPF && correspondePerfil && correspondeStatus) {
                linha.style.display = "";
            } else {
                linha.style.display = "none";
            }
        }
    }

    function limparFiltrosFunc() {
        filtroNome.value = "";
        filtroCPF.value = "";
        filtroPerfil.value = "";
        filtroStatus.value = "";
        filtrarUsuarios();
    }

    filtroNome.addEventListener("input", filtrarUsuarios);
    filtroCPF.addEventListener("input", filtrarUsuarios);
    filtroPerfil.addEventListener("change", filtrarUsuarios);
    filtroStatus.addEventListener("change", filtrarUsuarios);
    limparFiltros.addEventListener("click", limparFiltrosFunc);
});
