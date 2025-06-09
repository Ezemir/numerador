$(function () {

    $('.data').mask('99/99/9999', { placeholder: "__/__/__" });
    $('.hora').mask('99:99:99', { placeholder: "__:__:__" });
    $('.data_hora').mask('99/99/9999 99:99:99');
    $('.cep').mask('99999-999', { placeholder: "_____-___" });
    $('.fone').mask('(99)?99999-9999', { placeholder: "(__)_____-____" });
    $('.cel').mask('(99)99999-9999', { placeholder: "(__)_____-____" });
    $('.cpf').mask('999.999.999-99', { placeholder: "___.___.___-__" }, { reverse: true });
    $('.cnpj').mask('99.999.999/9999-99', { placeholder: "__.___.___/____-__" }, { reverse: true });
    $('.moeda').mask('999.999.999.999.999,99', { reverse: true });
    $('.moeda2').mask("#.##9,99", { reverse: true });  
    $('.percentual').mask('##9,99%', { reverse: true });

});