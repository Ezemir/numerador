// Tempo restante em segundos (20 minutos)
let tempoRestante = 2000 * 60;

function contador() {
    let minutos = Math.floor(tempoRestante / 60);
    let segundos = tempoRestante % 60;

    document.getElementById("tempo-sessao").textContent = 
        `${minutos}:${segundos.toString().padStart(2, '0')}`;

    if (tempoRestante > 0) {
        tempoRestante--;
        setTimeout(contador, 1000);
    } else {
        alert("Sua sessão expirou! Faça login novamente.");
        window.location.href = "/numerador/logout";
    }
}

// Espera o DOM carregar para iniciar o contador e abrir modal, se necessário
document.addEventListener("DOMContentLoaded", function() {
    contador();

    // Essa flag deve ser passada do Thymeleaf no template (true/false)
    var abrirModalAlterarSenhaFlag = /*[[${abrirModal}]]*/ false;

    if (abrirModalAlterarSenhaFlag) {
        abrirModalAlterarSenha();
    }
});

document.addEventListener('DOMContentLoaded', () => {
    fetch('/numerador/documentos/ultimos')
        .then(response => response.json())
        .then(data => {
            const tabela = document.getElementById('tabelaUltimosDocumentos');
            tabela.innerHTML = '';

            data.forEach(doc => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <td style="border: 1px solid #ccc; padding: 8px;">${doc.usuario.nome}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${doc.tipoDocumento.nome}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${doc.numero}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${doc.cartorio.nome}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${new Date(doc.dataCadastro).toLocaleString()}</td>
                `;

                tabela.appendChild(row);
            });
        })
        .catch(error => console.error('Erro ao carregar os últimos documentos:', error));
});
