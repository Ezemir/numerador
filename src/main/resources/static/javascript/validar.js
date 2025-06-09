document.addEventListener("DOMContentLoaded", function () {
    const codigoInput = document.getElementById("codigoValidador");

    if (!codigoInput) {
        console.error("Elemento Código de Validação não encontrado!");
        return;
    }

    console.log("Código de Validação.js carregado corretamente");

    // Define o placeholder inicial
    codigoInput.placeholder = "- - -  - - -";

    // Máscara para o código
    codigoInput.addEventListener("input", function () {
        let codigo = codigoInput.value.replace(/\D/g, ""); // Remove tudo que não for número

        if (codigo.length > 6) {
            codigo = codigo.slice(0, 6); // Limita a 6 dígitos
        }

        codigoInput.value = aplicarMascara(codigo);
    });

    // Validação antes de enviar o formulário
    document.querySelector(".login-form").addEventListener("submit", function (event) {
        // Remove os caracteres da máscara antes de enviar
        const codigo = codigoInput.value.replace(/\D/g, ""); 

        if (!validarCodigo(codigo)) {
            event.preventDefault();  // Impede o envio do formulário
            alert("Código de validação inválido! Verifique e tente novamente.");
        } else {
            // Antes de enviar, garantir que apenas os números sejam enviados
            codigoInput.value = codigo;  // Remover a máscara e deixar só os números
        }
    });
});

function aplicarMascara(codigo) {
    return codigo
        .replace(/(\d{3})(\d)/, "$1 $2");
}

function validarCodigo(codigo) {
    // O código precisa ser composto apenas de 6 dígitos numéricos
    const regex = /^\d{6}$/;
    return regex.test(codigo);
}
