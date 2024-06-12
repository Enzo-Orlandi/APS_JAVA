import javax.swing.*;
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;

public class Main {
    static int posLegendas = 0;
    static int tempo = 0;
    static int posValorMensal = 0;
    static int posMetaMensal = 0;
    static int posSelected = 0;
    static double metaAnual = 0;
    static double novaMeta = 0;
    static double somador = 0;
    static String convercao;
    static double[] valorMensal = {0,0,0,0,0,0,0,0,0,0,0,0};
    static double[] metaMensal = {0,0,0,0,0,0,0,0,0,0,0,0};

    static JTextField entrada = new JTextField();

    public static void main(String[] args) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] legendas = {"Insira a meta anual:", "Insira o valor atingido em janeiro:",
                "Insira o valor atingido em feveriro:", "Insira o valor atingido em março:",
                "Insira o valor atingido em abril:", "Insira o valor atingido em maio:",
                "Insira o valor atingido em junho:", "Insira o valor atingido em julho:",
                "Insira o valor atingido em agosto:", "Insira o valor atingido em setembro:",
                "Insira o valor atingido em outubro:", "Insira o valor atingido em novembro:",
                "Insira o valor atingido em dezembro:"};
        String[] meses = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro",
                "Outubro","Novembro","Dezembro"};
        String[] selected = {"Investimento em milhar (R$)", "Água enconomizada em M³"};
        JFrame f = new JFrame("APS - Atividade Prática Supervisionada");

        JPanel painelTitulo = new JPanel(null);
        JPanel painelSelect = new JPanel(null);
        JPanel painelInterativo = new JPanel(null);
        JPanel painelGrafico = new JPanel(null);

        JLabel lTitulo1 = new JLabel("Programa para simulação de metas ESG");
        JLabel lTitulo2 = new JLabel("(baseado no procedimento específico XXX)");
        JLabel lSelect = new JLabel("Selecione uma das áreas:");
        JLabel lInterativo = new JLabel(legendas[posLegendas]);

        JRadioButton esg1 = new JRadioButton("ISP", true);
        JRadioButton esg2 = new JRadioButton("CCA");

        ButtonGroup select = new ButtonGroup();

        JButton bInserir = new JButton("Inserir");
        JButton bExcluir = new JButton("Excluir ultimo");
        JButton bReset = new JButton("Reset");

        JFreeChart grafico = ChartFactory.createBarChart(
                selected[posSelected],                         // chart title
                "Mês",                         // domain axis label
                "Quantidade",                         // range axis label
                dataset,            // data
                PlotOrientation.VERTICAL,   // orientation
                true,                       // include legend
                true,                       // tooltips?
                false                       // URLs?
        );
        ChartPanel chartPanel = new ChartPanel(grafico);
        chartPanel.setBounds(10,10,715,640);

        lTitulo1.setBounds(25,0,500,50);
        lTitulo1.setFont(new Font("Arial", Font.BOLD, 22));
        lTitulo2.setBounds(15,40,500,50);
        lTitulo2.setFont(new Font("Arial", Font.BOLD, 22));
        lSelect.setBounds(5,0,400,100);
        lSelect.setFont(new Font("Arial", Font.BOLD, 22));
        lInterativo.setBounds(5,0,400,70);
        lInterativo.setFont(new Font("Arial", Font.BOLD, 21));

        esg1.setBounds(300,0,80,100);
        esg1.setFont(new Font("Arial", Font.BOLD,22));
        esg1.setBackground(Color.YELLOW);
        esg1.addActionListener(e -> {
            posSelected=0;
            grafico.setTitle(selected[posSelected]);
        });
        esg2.setBounds(400,0,80,100);
        esg2.setFont(new Font("Arial", Font.BOLD,22));
        esg2.setBackground(Color.YELLOW);
        esg2.addActionListener(e -> {
            posSelected=1;
            grafico.setTitle(selected[posSelected]);
        });
        select.add(esg1);
        select.add(esg2);

        entrada.setBounds(390,10,100,50);
        entrada.setFont(new Font("Arial", Font.PLAIN,20));

        bInserir.setBounds(390,100,100,50);
        bInserir.setFont(new Font("Arial", Font.BOLD,20));
        bInserir.addActionListener(e -> {
            convercao = entrada.getText();
            if (posLegendas == 0){
                metaAnual = Double.parseDouble(convercao);
                metaAnual = metaAnual/12;
                for (posMetaMensal=0;posMetaMensal<12;posMetaMensal++){
                    metaMensal[posMetaMensal]=metaAnual;
                    dataset.addValue(metaMensal[posMetaMensal],"Meta",meses[posMetaMensal]);
                }
                metaAnual = metaAnual*12;
                posMetaMensal = 0;
            }
            if ((posLegendas >= 1)&&(posLegendas<13)){
                valorMensal[posValorMensal] = Double.parseDouble(convercao);
                if (valorMensal[posValorMensal] != metaMensal[posValorMensal]) {
                    somador = 0;
                    for (tempo = posValorMensal; tempo >= 0; tempo--) {
                        somador = somador + valorMensal[tempo];
                    }
                    novaMeta = (metaAnual - somador) / (12 - (posValorMensal + 1));
                    if (novaMeta >= 0) {
                        for (posMetaMensal = (posValorMensal + 1); posMetaMensal < 12; posMetaMensal++) {
                            metaMensal[posMetaMensal] = novaMeta;
                            dataset.addValue(metaMensal[posMetaMensal], "Meta", meses[posMetaMensal]);
                        }
                    }
                }
                dataset.addValue(valorMensal[posValorMensal], "Atingido", meses[posValorMensal]);
                posValorMensal++;
                posMetaMensal = posValorMensal;
            }
            if (posLegendas > 12){
                posLegendas = -1;
                posValorMensal = 0;
                for (posMetaMensal=0;posMetaMensal<12;posMetaMensal++){
                    dataset.addValue(0,"Meta",meses[posMetaMensal]);
                    dataset.addValue(0,"Atingido",meses[posMetaMensal]);
                }
            }
            posLegendas++;
            lInterativo.setText(legendas[posLegendas]);
        });
        bExcluir.setBounds(200,100,100,50);
        bExcluir.setFont(new Font("Arial", Font.BOLD,20));
        bExcluir.addActionListener(e -> {
            if (posLegendas>0){
                if (posLegendas>posValorMensal){
                    posValorMensal--;
                    dataset.addValue(0,"Atingido",meses[posValorMensal]);
                }
                posLegendas--;
            }
            lInterativo.setText(legendas[posLegendas]);
        });
        bReset.setBounds(10,100,100,50);
        bReset.setFont(new Font("Arial", Font.BOLD,20));
        bReset.addActionListener(e -> {
            posLegendas=0;
            posValorMensal=0;
            lInterativo.setText(legendas[posLegendas]);
            for (posMetaMensal=0;posMetaMensal<12;posMetaMensal++){
                dataset.addValue(0,"Meta",meses[posMetaMensal]);
                dataset.addValue(0,"Atingido",meses[posMetaMensal]);
            }
        });


        painelTitulo.setBounds(10,10,500,100);
        painelTitulo.setBackground(Color.YELLOW);
        painelTitulo.add(lTitulo1);
        painelTitulo.add(lTitulo2);

        painelSelect.setBounds(10,120,500,100);
        painelSelect.setBackground(Color.YELLOW);
        painelSelect.add(lSelect);
        painelSelect.add(esg1);
        painelSelect.add(esg2);

        painelInterativo.setBounds(10,230,500,200);
        painelInterativo.setBackground(Color.YELLOW);
        painelInterativo.add(entrada);
        painelInterativo.add(lInterativo);
        painelInterativo.add(bInserir);
        painelInterativo.add(bExcluir);
        painelInterativo.add(bReset);

        painelGrafico.setBounds(520,10,735,660);
        painelGrafico.setBackground(Color.YELLOW);
        painelGrafico.add(chartPanel);

        f.add(painelTitulo);
        f.add(painelSelect);
        f.add(painelInterativo);
        f.add(painelGrafico);
        f.setSize(1280,720);
        f.setLayout(null);
        f.setVisible(true);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setBackground(Color.BLUE);

    }
}