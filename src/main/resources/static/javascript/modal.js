function abrirModalCadastrarUsuarios() {
    var modal = new bootstrap.Modal(document.getElementById('modalCadastrarUsuarios'));
    modal.show();
}

function abrirModalCadastrarCartorios() {
    var modal = new bootstrap.Modal(document.getElementById('modalCadastrarCartorios'));
	modal.show();
}

function abrirModalTipoDocumento() {
    var modal = new bootstrap.Modal(document.getElementById('modalTipoDocumento'));
    modal.show();
}

function abrirModalAlterarSenha() {
   var modal = new bootstrap.Modal(document.getElementById('modalAlterarSenha'));
   modal.show();
}

function abrirModalLogout() {
	   var modal = new bootstrap.Modal(document.getElementById('modalLogout'));
	   modal.show();
	}

function abrirModalGerarDocumento(id, nome) {
    document.getElementById('tipoDocumentoId').value = id;
    document.getElementById('nomeDocumento').value = nome;

    var modal = new bootstrap.Modal(document.getElementById('modalGerarDocumento'));
    modal.show();
}