document.addEventListener("DOMContentLoaded", function () {
    const cpfInput = document.getElementById("cpf");

    if (!cpfInput) {
        console.error("Elemento CPF não encontrado!");
        return;
    }

    console.log("CPF.js carregado corretamente");

    // Define o placeholder inicial
    cpfInput.placeholder = "000.000.000-00";

    cpfInput.addEventListener("input", function () {
        let cpf = cpfInput.value.replace(/\D/g, ""); // Remove tudo que não for número

        if (cpf.length > 11) {
            cpf = cpf.slice(0, 11);
        }

        cpfInput.value = formatarCPF(cpf);
    });

    document.querySelector(".login-form").addEventListener("submit", function () {
        cpfInput.value = cpfInput.value.replace(/\D/g, ""); // Remove pontos e traços antes do envio
    });
});

function formatarCPF(cpf) {
    return cpf
        .replace(/(\d{3})(\d)/, "$1.$2")
        .replace(/(\d{3})(\d)/, "$1.$2")
        .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
}
