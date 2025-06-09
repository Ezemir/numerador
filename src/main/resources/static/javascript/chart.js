document.addEventListener('DOMContentLoaded', () => {
    const cores = [
        '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF',
        '#FF9F40', '#E7E9ED', '#76B041', '#FF6F61', '#6A5ACD'
    ];

    // Gráfico 1: Tipos de Documentos
    const ctxTipos = document.getElementById('graficoTiposDocumentos').getContext('2d');
    const myChartTipos = new Chart(ctxTipos, {
        type: 'bar',
        data: {
            labels: nomes,
            datasets: [{
                label: 'Quantidade',
                data: quantidades,
                backgroundColor: nomes.map((_, i) => cores[i % cores.length]),
                borderColor: nomes.map((_, i) => cores[i % cores.length]),
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: { display: false },
                y: { beginAtZero: true, ticks: { stepSize: 1 } }
            },
            plugins: {
                legend: {
                    display: true,
                    position: 'right',
                    labels: {
                        generateLabels: (chart) => chart.data.labels.map((label, i) => ({
                            text: label,
                            fillStyle: cores[i % cores.length],
                            strokeStyle: cores[i % cores.length],
                            index: i
                        })),
                        boxWidth: 20,
                        padding: 15,
                        font: { size: 14 }
                    }
                },
                tooltip: {
                    enabled: true,
                    callbacks: {
                        label: context => `${context.label}: ${context.parsed.y}`
                    }
                }
            }
        }
    });
    
 // Gráfico 2: Documentos por Cartório (Barra Horizontal)
    const ctxCartorios = document.getElementById('graficoPorCartorio').getContext('2d');
    const myChartCartorios = new Chart(ctxCartorios, {
        type: 'bar',
        data: {
            labels: nomesCartorios,
            datasets: [{
                label: 'Documentos Produzidos',
                data: quantidadesCartorios,
                backgroundColor: nomesCartorios.map((_, i) => cores[i % cores.length]),
                borderColor: nomesCartorios.map((_, i) => cores[i % cores.length]),
                borderWidth: 1
            }]
        },
        options: {
            indexAxis: 'y',
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: { beginAtZero: true, ticks: { stepSize: 1 } },
                y: { ticks: { autoSkip: false } }
            },
            plugins: {
                legend: { display: false },  // ✅ Legenda desativada
                tooltip: {
                    enabled: true,
                    callbacks: {
                        label: context => `${context.label}: ${context.parsed.x}`
                    }
                }
            }
        }
    });
});
