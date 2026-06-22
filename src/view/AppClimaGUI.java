package view;

import model.Cidade;
import model.ListaCidades;
import model.Previsao;
import model.PrevisaoCidade;
import model.parse.XStreamParser;
import model.service.IbgeService;
import model.service.WeatherForecastService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AppClimaGUI extends JFrame {

    private JComboBox<String> comboEstados;
    private JTextField txtFiltro;
    private JComboBox<String> comboCidadesIbge;
    private JPanel panelPrevisao;

    private List<String> todasCidadesDoEstado = new ArrayList<>();

    private final Color COR_FUNDO = new Color(18, 18, 18);
    private final Color COR_CARD = new Color(33, 33, 33);
    private final Color COR_TEXTO_PRINCIPAL = new Color(255, 255, 255);
    private final Color COR_TEXTO_SECUNDARIO = new Color(176, 176, 176);
    private final Color COR_BORDA = new Color(60, 60, 60);
    private final Color COR_INPUT = new Color(45, 45, 45);

    public AppClimaGUI() {
        setTitle("App de Clima");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COR_FUNDO);

        initUI();
        
        comboEstados.setSelectedItem("MG");
    }

    private void initUI() {
        JPanel panelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelTopo.setBackground(COR_FUNDO);
        panelTopo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COR_BORDA));

        JLabel lblEstado = criarLabel("Estado:");
        comboEstados = new JComboBox<>(IbgeService.ESTADOS);
        estilizarComponente(comboEstados);

        JLabel lblFiltro = criarLabel("Filtrar:");
        txtFiltro = new JTextField("Machado", 10);
        estilizarComponente(txtFiltro);

        JLabel lblCidade = criarLabel("Cidade:");
        comboCidadesIbge = new JComboBox<>();
        comboCidadesIbge.setPreferredSize(new Dimension(220, 32));
        estilizarComponente(comboCidadesIbge);

        JButton btnVerClima = new JButton("Ver Clima");
        btnVerClima.setBackground(new Color(40, 167, 69));
        btnVerClima.setForeground(Color.WHITE);
        btnVerClima.setFocusPainted(false);
        btnVerClima.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panelTopo.add(lblEstado);
        panelTopo.add(comboEstados);
        panelTopo.add(lblFiltro);
        panelTopo.add(txtFiltro);
        panelTopo.add(lblCidade);
        panelTopo.add(comboCidadesIbge);
        panelTopo.add(btnVerClima);

        add(panelTopo, BorderLayout.NORTH);

        panelPrevisao = new JPanel(new BorderLayout(15, 15));
        panelPrevisao.setBackground(COR_FUNDO);
        panelPrevisao.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(panelPrevisao, BorderLayout.CENTER);

        comboEstados.addActionListener(e -> carregarCidadesDoEstado());

        txtFiltro.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarCidades(); }
            public void removeUpdate(DocumentEvent e) { filtrarCidades(); }
            public void changedUpdate(DocumentEvent e) { filtrarCidades(); }
        });

        btnVerClima.addActionListener(e -> exibirPrevisao());
    }

    private void carregarCidadesDoEstado() {
        String uf = (String) comboEstados.getSelectedItem();
        if (uf == null) return;

        SwingWorker<List<String>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                return IbgeService.getCidades(uf);
            }
            @Override
            protected void done() {
                try {
                    todasCidadesDoEstado = get();
                    filtrarCidades();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AppClimaGUI.this, "Erro ao buscar cidades no IBGE.");
                }
            }
        };
        worker.execute();
    }

    private void filtrarCidades() {
        String termo = txtFiltro.getText().toLowerCase();
        comboCidadesIbge.removeAllItems();

        for (String cidade : todasCidadesDoEstado) {
            if (cidade.toLowerCase().contains(termo)) {
                comboCidadesIbge.addItem(cidade);
            }
        }
        
        if (comboCidadesIbge.getItemCount() > 0) {
            comboCidadesIbge.setSelectedIndex(0);
        }
    }

    private void exibirPrevisao() {
        String cidadeSelecionada = (String) comboCidadesIbge.getSelectedItem();
        String estadoSelecionado = (String) comboEstados.getSelectedItem();

        if (cidadeSelecionada == null || estadoSelecionado == null) return;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                String xmlCidades = WeatherForecastService.cidades(cidadeSelecionada);
                XStreamParser<PrevisaoCidade, ListaCidades> parserCidades = new XStreamParser<>();
                ListaCidades lista = parserCidades.cidades(xmlCidades);

                Cidade cidadeEncontrada = null;
                if (lista != null && lista.getCidades() != null) {
                    for (Cidade c : lista.getCidades()) {
                        if (c.getUf().equalsIgnoreCase(estadoSelecionado) && c.getNome().equalsIgnoreCase(cidadeSelecionada)) {
                            cidadeEncontrada = c;
                            break;
                        }
                    }
                }

                if (cidadeEncontrada == null) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(AppClimaGUI.this, 
                        "Infelizmente o INPE não possui dados climáticos para esta cidade."));
                    return null;
                }

                final Cidade cidadeFinalParaLambda = cidadeEncontrada;

                String xmlPrevisao = WeatherForecastService.previsoesParaSeteDias(cidadeFinalParaLambda.getId());
                XStreamParser<PrevisaoCidade, ListaCidades> parserPrevisao = new XStreamParser<>();
                PrevisaoCidade previsaoCidade = parserPrevisao.previsao(xmlPrevisao);

                List<Previsao> previsoes = previsaoCidade.getPrevisoes();
                
                SwingUtilities.invokeLater(() -> atualizarPainelPrevisao(previsoes, cidadeFinalParaLambda));
                
                return null;
            }
        };
        worker.execute();
    }

    private void atualizarPainelPrevisao(List<Previsao> previsoes, Cidade c) {
        panelPrevisao.removeAll();

        if (previsoes != null && !previsoes.isEmpty()) {
            Previsao hoje = previsoes.get(0);
            List<Previsao> proximosDias = previsoes.subList(1, previsoes.size());

            panelPrevisao.add(criarCardHoje(hoje, c), BorderLayout.NORTH);

            JPanel panelOutrosDias = new JPanel(new GridLayout(1, proximosDias.size(), 10, 0));
            panelOutrosDias.setBackground(COR_FUNDO);
            for (Previsao p : proximosDias) {
                panelOutrosDias.add(criarCardDia(p));
            }
            
            panelPrevisao.add(panelOutrosDias, BorderLayout.CENTER);
        }

        panelPrevisao.revalidate();
        panelPrevisao.repaint();
    }

    private JPanel criarCardHoje(Previsao p, Cidade c) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(COR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1, true),
                new EmptyBorder(25, 20, 25, 20)));

        JLabel lblTitulo = new JLabel(c.getNome() + " - " + c.getUf() + " (" + formataData(p.getDia()) + ")");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(COR_TEXTO_SECUNDARIO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelCentro.setBackground(COR_CARD);

        JLabel lblIcone = new JLabel("Carregando...");
        lblIcone.setForeground(COR_TEXTO_SECUNDARIO);
        carregarImagemAsync(DicionarioClima.getIconeUrl(p.getTempo()), lblIcone);

        int max = 0, min = 0;
        try {
            max = Integer.parseInt(p.getMaxima());
            min = Integer.parseInt(p.getMinima());
        } catch (NumberFormatException e) { }
        int tempAtual = (max + min) / 2; 

        JLabel lblTempAtual = new JLabel(tempAtual + "°C");
        lblTempAtual.setFont(new Font("Segoe UI", Font.BOLD, 65));
        lblTempAtual.setForeground(COR_TEXTO_PRINCIPAL);

        panelCentro.add(lblIcone);
        panelCentro.add(lblTempAtual);

        String desc = DicionarioClima.getDescricao(p.getTempo());
        JLabel lblTemps = new JLabel(desc + "   |   Máx: " + max + "°C   Mín: " + min + "°C");
        lblTemps.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblTemps.setForeground(COR_TEXTO_SECUNDARIO);
        lblTemps.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(panelCentro, BorderLayout.CENTER);
        card.add(lblTemps, BorderLayout.SOUTH);

        return card;
    }

    private JPanel criarCardDia(Previsao p) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1, true),
                new EmptyBorder(15, 5, 15, 5)));

        JLabel lblData = new JLabel(formataData(p.getDia()));
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblData.setForeground(COR_TEXTO_PRINCIPAL);
        lblData.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblIcone = new JLabel();
        lblIcone.setHorizontalAlignment(SwingConstants.CENTER);
        carregarImagemAsync(DicionarioClima.getIconeUrl(p.getTempo()), lblIcone);

        JLabel lblTemps = new JLabel("<html><center><font color='#FF6B6B'>▲ " + p.getMaxima() + "°</font> &nbsp; <font color='#4DABF7'>▼ " + p.getMinima() + "°</font></center></html>");
        lblTemps.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTemps.setHorizontalAlignment(SwingConstants.CENTER);
        
        String desc = DicionarioClima.getDescricao(p.getTempo());
        JLabel lblDesc = new JLabel("<html><center>" + desc + "</center></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(COR_TEXTO_SECUNDARIO);
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
        lblDesc.setPreferredSize(new Dimension(90, 40));

        card.add(lblData, BorderLayout.NORTH);
        card.add(lblIcone, BorderLayout.CENTER);
        
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(COR_CARD);
        pnlBottom.add(lblTemps, BorderLayout.NORTH);
        pnlBottom.add(lblDesc, BorderLayout.SOUTH);
        card.add(pnlBottom, BorderLayout.SOUTH);

        return card;
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(COR_TEXTO_PRINCIPAL);
        return label;
    }

    private void estilizarComponente(JComponent comp) {
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comp.setBackground(COR_INPUT);
        comp.setForeground(COR_TEXTO_PRINCIPAL);
        if (comp instanceof JTextField) {
            ((JTextField) comp).setCaretColor(COR_TEXTO_PRINCIPAL);
            comp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COR_BORDA),
                    new EmptyBorder(5, 5, 5, 5)));
        }
    }

    private void carregarImagemAsync(String urlString, JLabel label) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                return new ImageIcon(new URL(urlString));
            }
            @Override
            protected void done() {
                try {
                    label.setText("");
                    label.setIcon(get());
                } catch (Exception e) {
                    label.setText("Sem ícone");
                }
            }
        };
        worker.execute();
    }
    
    private String formataData(String dataInpe) {
        String[] parts = dataInpe.split("-");
        if(parts.length == 3) {
            return parts[2] + "/" + parts[1];
        }
        return dataInpe;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new AppClimaGUI().setVisible(true));
    }
}