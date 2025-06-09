document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".btn-gerar-qrcode").forEach(button => {
        button.addEventListener("click", function () {
            let userId = this.getAttribute("data-id");
            
            // Habilitar 2FA e gerar QR Code
            fetch(`/usuarios/${userId}/enable2fa`, { method: "POST" })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Erro ao habilitar 2FA");
                    }
                    return response.text(); // A resposta pode ser um redirecionamento para o QR Code
                })
                .then(() => {
                    // Após habilitar, busca a imagem do QR Code
                    return fetch(`/usuarios/${userId}/qrcode`);
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Erro ao obter QR Code");
                    }
                    return response.blob();
                })
                .then(blob => {
                    let qrCodeUrl = URL.createObjectURL(blob);
                    document.getElementById("qrCodeImage").src = qrCodeUrl;
                    document.getElementById("qrCodeContainer").style.display = "block";
                })
                .catch(error => console.error(error));
        });
    });
});
