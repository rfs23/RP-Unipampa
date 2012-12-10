/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import CN.AlocacaoPeca;
import CN.Divisao;
import CN.ItemPeca;
import CN.Peca;
import CN.Semeadora;
import CN.TipoAlocacao;
import CN.TipoPeca;
import Cadastro.CadastroItensPeca;
import Cadastro.CadastroPecas;
import Cadastro.CadastroSemeadoras;
import Exceções.AtualizacaoException;
import Exceções.DelecaoException;
import Exceções.InsercaoException;
import Exceções.RegistroException;
import Repositório.AcessoPostgres;
import Repositório.DBItemPeca;
import Repositório.DBPeca;
import Repositório.DBSemeadora;
import Utilitários.DateConversion;
import Utilitários.ModelString;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Painter;
import javax.swing.UIDefaults;

/**
 *
 * @author rafael
 */
public class FramePrincipal extends javax.swing.JFrame {

    /**
     * Creates new form FramePrincipal
     */
    private static CadastroSemeadoras cadSem;
    private static CadastroItensPeca cadItensPeca;
    private static CadastroPecas cadPeca;
    private Semeadora semeadora;

    static {

        cadSem = new CadastroSemeadoras(new DBSemeadora(AcessoPostgres.getInstance()));
        cadItensPeca = new CadastroItensPeca(new DBItemPeca(AcessoPostgres.getInstance()));
        cadPeca = new CadastroPecas(new DBPeca(AcessoPostgres.getInstance()));
    }

    public FramePrincipal() {

        initComponents();
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.semeadora = new Semeadora("a", "b", 2000);
    }

    private void centralizarInternalFrame(JInternalFrame frame) {

        Dimension dmDesk = this.jDesktopPane1.getSize();
        Dimension dmFrame = frame.getSize();

        frame.setLocation((dmDesk.width - dmFrame.width) / 2, (dmDesk.height - dmFrame.height) / 2);
    }

    private void preencheComboPecas(JComboBox cbPecas, TipoPeca tp) {

        cbPecas.removeAllItems();
        cadItensPeca.selectItensPecaNaoAlocadosByTipo(tp);

        for (ItemPeca ip : CadastroItensPeca.itensPeca.values()) {

            if (this.semeadora.selecionarPeca(ip.getIdentificacao()) != null) {

                continue;
            }

            String descItemPeca = "" + ip.getIdentificacao() + " - " + ip.getPeca().getFabricante() + " - " + ip.getAnoFab() + " - " + ip.getTempoVidaUtilRestante();
            ModelString msDescItemPeca = new ModelString(descItemPeca, ip.getIdentificacao());
            cbPecas.addItem(msDescItemPeca);
        }
    }

    private boolean verificaAno(JTextField tfAno) throws NumberFormatException {

        try {

            Integer.parseInt(tfAno.getText());
            return true;
        } catch (NumberFormatException nfe) {

            JOptionPane.showMessageDialog(null, "O ano informado é inválido. O ano é composto por 4 dígitos!");
            tfAno.requestFocus();
            return false;
        }
    }

    private boolean verificaData(JTextField tfData) {

        try {

            obtemData(tfData.getText());
            return true;
        } catch (RuntimeException ex) {

            JOptionPane.showMessageDialog(null, "A data informada é inválida. \n" + "Deve seguir o modelo ##/##/####");
            tfData.requestFocus();
            return false;
        }
    }

    private boolean verificaCampoVazio(JTextField tfCampo) {

        try {

            if (tfCampo.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Não é admitido campo vazio");
                tfCampo.requestFocus();
                return false;
            }

            return true;
        } catch (RuntimeException rte) {

            JOptionPane.showMessageDialog(null, rte.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private Date obtemData(String strData) throws RuntimeException {

        String[] subData = strData.split("/");
        return new Date(Integer.parseInt(subData[2]) - 1900, Integer.parseInt(subData[1]) - 1, Integer.parseInt(subData[0]));
    }

    private ItemPeca obtemItemPeça(JComboBox cbItensPeça) {

        try {

            if (cbItensPeça.getSelectedIndex() != -1) {

                if (cbItensPeça.getSelectedItem() instanceof ModelString) {

                    int coditemPeca = ((ModelString) cbItensPeça.getSelectedItem()).getCodigo();
                    return cadItensPeca.selectItemPeca(coditemPeca);
                }

                return null;
            }

            JOptionPane.showMessageDialog(null, "Deve ser selecionada uma peça");
            cbItensPeça.requestFocus();
            return null;
        } catch (RuntimeException rte) {

            JOptionPane.showMessageDialog(null, rte.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void ajustaCamposCadSemeadora() {

        semeadora = new Semeadora("a", "b", 2010);
        tfMarcaSem.setText("");
        tfModelo.setText("");
        ftfAnoSem.setText("");
        Date dataAtual = new Date();
        ftfDataRegistro.setText(DateConversion.dateToString(dataAtual));
        tfCodSem.setText("" + cadSem.gerarCódigoSemeadora());
        preencheComboPecas(cbDiscoDosador, TipoPeca.DISCO_DOSADOR);
        refreshListaDivisoes(semeadora, listDivisoes);
        ftfAnoSem.requestFocus();
    }

    public void abreFrameCadSemeadora() {

        if (!iFrameCadSemeadora.isVisible()) {

            this.iFrameCadSemeadora.setVisible(true);
            centralizarInternalFrame(iFrameCadSemeadora);
            ajustaCamposCadSemeadora();
        }
    }

    public void incluirSemeadora() {

        if (verificaAno(ftfAnoSem) && verificaData(ftfDataRegistro) && verificaCampoVazio(tfModelo) && verificaCampoVazio(tfMarcaSem)) {

            try {

                this.semeadora.setIdentificacao(Integer.parseInt(tfCodSem.getText()));
                this.semeadora.setAno(Integer.parseInt(ftfAnoSem.getText()));
                this.semeadora.setMarca(tfMarcaSem.getText());
                this.semeadora.setModelo(tfModelo.getText());
                this.semeadora.setDataRegistro(obtemData(ftfDataRegistro.getText()));

                if (obtemItemPeça(cbDiscoDosador) != null) {

                    this.semeadora.addDivisao("Semeadora", TipoAlocacao.Semeadora);
                    this.semeadora.addPeca(semeadora.nextCodeDivisao() - 1, obtemItemPeça(cbDiscoDosador));
                    cadSem.insertSemeadora(semeadora);
                    JOptionPane.showMessageDialog(null, "Semeadora incluída com sucesso");
                    this.semeadora = new Semeadora("a", "b", new Date().getYear() + 1900);
                    ajustaCamposCadSemeadora();
                }

            } catch (RegistroException re) {

                JOptionPane.showMessageDialog(null, re.getMessage());
                JOptionPane.showMessageDialog(null, re.getRTException().getMessage());
            } catch (RuntimeException rte) {

                JOptionPane.showMessageDialog(null, rte.getMessage());

            }
        }
    }

    public void refreshListaDivisoes(Semeadora semeadora, JList listDivisoes) {

        listDivisoes.removeAll();
        DefaultListModel dlm = new DefaultListModel();

        for (Divisao div : semeadora.listarDivisoes()) {

            ModelString msDiv = new ModelString(div.getIdentificao() + " - " + div.getTipoAloc() + " - " + div.getNome(), div.getIdentificao());
            dlm.addElement(msDiv);
        }

        listDivisoes.setModel(dlm);
    }

    public void abreFrameAddDivisao() {

        if (!iFrameAddDivisao.isVisible()) {

            iFrameAddDivisao.setVisible(true);
            centralizarInternalFrame(iFrameAddDivisao);
            refreshCombosPecasDivisao();
        }

    }

    private void refreshCombosPecasDivisao() {

        preencheComboPecas(cbDiscoCortePalhada, TipoPeca.DISCO_DE_CORTE_DE_PALHADA);
        preencheComboPecas(cbEjetorSementes, TipoPeca.EJETOR_DE_SEMENTES);
        preencheComboPecas(cbPonteira, TipoPeca.PONTEIRA);
        preencheComboPecas(cbRolamento, TipoPeca.ROLAMENTO);
        preencheComboPecas(cbDosadorFertilizante, TipoPeca.DOSADOR_DE_FERTILIZANTE);
        preencheComboPecas(cbDiscoDuploTosado, TipoPeca.DISCO_DUPLO_DE_TOSADO);
    }

    public boolean verificaItemSelecionado(JComboBox cb) {

        if (cb.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(null, "Deve ser selecionado um item", null, JOptionPane.WARNING_MESSAGE);
            cb.requestFocus();
            return false;
        }

        return true;
    }

    public boolean incluirDivisaoLinha() {

        if (verificaItemSelecionado(cbDiscoCortePalhada) && verificaItemSelecionado(cbPonteira) && verificaItemSelecionado(cbEjetorSementes) && verificaItemSelecionado(cbRolamento) && verificaItemSelecionado(cbDosadorFertilizante) && verificaItemSelecionado(cbDiscoDuploTosado)) {

            semeadora.addDivisao(semeadora.nextCodeDivisao(), "Linha " + semeadora.nextCodeDivisao(), TipoAlocacao.Linha);
            semeadora.addPeca(semeadora.nextCodeDivisao() - 1, obtemItemPeça(cbDiscoCortePalhada), new Date());
            semeadora.addPeca(semeadora.nextCodeDivisao() - 1, obtemItemPeça(cbEjetorSementes), new Date());
            semeadora.addPeca(semeadora.nextCodeDivisao() - 1, obtemItemPeça(cbPonteira), new Date());
            semeadora.addPeca(semeadora.nextCodeDivisao() - 1, obtemItemPeça(cbRolamento), new Date());
            semeadora.addPeca(semeadora.nextCodeDivisao() - 1, obtemItemPeça(cbDosadorFertilizante), new Date());
            semeadora.addPeca(semeadora.nextCodeDivisao() - 1, obtemItemPeça(cbDiscoDuploTosado), new Date());
            return true;
        }

        return false;
    }

    private void preencheListaSems(Collection<Semeadora> semeadoras) {

        DefaultListModel dlm = new DefaultListModel();
        if (semeadoras == null) {

            listConsultaSemSemeadoras.setModel(dlm);
            return;
        }


        for (Semeadora sem : semeadoras) {

            ModelString msSemeadoas = new ModelString("" + sem.getIdentificacao() + " - " + sem.getModelo() + " - " + sem.getMarca() + " - " + sem.getAno() + " - " + DateConversion.dateToString(sem.getDataRegistro()), sem.getIdentificacao());
            dlm.addElement(msSemeadoas);
        }

        listConsultaSemSemeadoras.setModel(dlm);
    }

    public Collection<Semeadora> consultaSemeadoras(String atributoBusca, Object valorBusca) {

        List<Semeadora> semeadoras = new ArrayList<Semeadora>();

        try {

            if (valorBusca != null) {

                if (atributoBusca.equals("Identificação")) {

                    int codSem = Integer.parseInt((String) valorBusca);
                    cadSem.selectSemeadora(codSem);

                    if (CadastroSemeadoras.semeadoras.get(codSem) != null) {

                        semeadoras.add(CadastroSemeadoras.semeadoras.get(Integer.parseInt((String) valorBusca)));
                        return semeadoras;
                    }
                } else if (atributoBusca.equals("Modelo")) {

                    cadSem.selectSemeadorasByModelo((String) valorBusca);
                    return CadastroSemeadoras.semeadoras.values();
                } else if (atributoBusca.equals("Marca")) {

                    cadSem.selectSemeadorasByMarca((String) valorBusca);
                    return CadastroSemeadoras.semeadoras.values();
                } else if (atributoBusca.equals("Ano")) {

                    cadSem.selectSemeadorasByAno(Integer.parseInt((String) valorBusca));
                    return CadastroSemeadoras.semeadoras.values();
                }

            }

            return null;
        } catch (Exception ex) {

            return null;
        }
    }

    public void abreFrameConsultaSem() {

        if (!iFrameConsultaSemeadora.isVisible()) {

            listConsultaSemSemeadoras.removeAll();
            cadSem.listSemeadoras();

            restauraCamposConsultaSem();

            centralizarInternalFrame(iFrameConsultaSemeadora);
            iFrameConsultaSemeadora.setVisible(true);
        }
    }

    private void restauraCamposConsultaSem() {

        preencheListaSems(CadastroSemeadoras.semeadoras.values());
        btConsultaSemSalvar.setVisible(false);
        btConsultaSemCancelar.setVisible(false);
        btConsultaSemExcluir.setVisible(true);
        btConsultaSemAlterar.setVisible(true);
        tfConsultaSemeadoraPesquisar.setEnabled(true);
        tfConsultaSemeadoraPesquisar.setText("");
        listConsultaSemSemeadoras.setEnabled(true);
        habilitaCamposAlteracaoSemeadora(false);
        tfConsultaSemeadoraPesquisar.requestFocus();
    }

    public void preencheCamposConsultaSemeadora(Semeadora sem) {

        tfCodSemConsulta.setText("" + sem.getIdentificacao());
        ftfAnoSemConsulta.setText("" + sem.getAno());
        tfModeloConsulta.setText(sem.getModelo());
        tfMarcaSemConsulta.setText(sem.getMarca());
        ftfDataRegistroConsulta.setText(DateConversion.dateToString(sem.getDataRegistro()));
        refreshListaDivisoes(sem, listConsultaSemDivisoes);
    }

    private Semeadora obtemSemeadora(Object obj) {

        if (obj instanceof ModelString) {

            int codSem = ((ModelString) obj).getCodigo();
            return CadastroSemeadoras.semeadoras.get(codSem);
        }

        return null;
    }

    private int obtemCodDeObjetoLista(Object obj) {

        if (obj instanceof ModelString) {

            int cod = ((ModelString) obj).getCodigo();
            return cod;
        }

        return -1;
    }

    private void limpaCamposConsultaSem() {

        tfCodSemConsulta.setText("");
        ftfAnoSemConsulta.setText("");
        tfModeloConsulta.setText("");
        tfMarcaSemConsulta.setText("");
        ftfDataRegistroConsulta.setText("");
        listConsultaSemDivisoes.setModel(new DefaultListModel());
        listConsultaSemPeca.setModel(new DefaultListModel());
    }

    public void habilitaCamposAlteracaoSemeadora(boolean habilita) {

        ftfAnoSemConsulta.setEditable(habilita);
        ftfDataRegistroConsulta.setEditable(habilita);
        tfMarcaSemConsulta.setEditable(habilita);
        tfModeloConsulta.setEditable(habilita);
    }

    public void preencheListaPeças(Collection<AlocacaoPeca> pecas, JList list) {

        DefaultListModel dlm = new DefaultListModel();
        for (AlocacaoPeca ap : pecas) {

            ItemPeca ip = ap.getItemPeca();
            ModelString msDescItemPeca = new ModelString("" + ip.getIdentificacao() + " - " + ip.getPeca().getTipo() + " - " + ip.getTempoVidaUtilRestante(), ip.getIdentificacao());
            dlm.addElement(msDescItemPeca);
        }

        list.setModel(dlm);
    }

    public boolean atualizarSemeadora() {

        int codSem = Integer.parseInt(tfCodSemConsulta.getText());

        Semeadora sem = CadastroSemeadoras.semeadoras.get(codSem);

        if (verificaAno(ftfAnoSemConsulta) && verificaData(ftfDataRegistroConsulta) && verificaCampoVazio(tfModeloConsulta) && verificaCampoVazio(tfMarcaSemConsulta)) {

            try {

                sem.setAno(Integer.parseInt(ftfAnoSemConsulta.getText()));
                sem.setDataRegistro(DateConversion.stringToDate(ftfDataRegistroConsulta.getText()));
                sem.setMarca(tfMarcaSemConsulta.getText());
                sem.setModelo(tfModeloConsulta.getText());
            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.WARNING_MESSAGE);
                return false;
            }

            try {

                cadSem.updateSemeadora(codSem, sem);
            } catch (AtualizacaoException ex) {

                JOptionPane.showMessageDialog(panelButtons, ex.getMessage(), "Erro ao alterar", JOptionPane.ERROR_MESSAGE);
                JOptionPane.showMessageDialog(panelButtons, ex.getException().getMessage(), "Erro ao alterar", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        return false;
    }

    public boolean excluiSemeadora() {

        int codSem = Integer.parseInt(tfCodSemConsulta.getText());

        try {

            cadSem.deleteSemeadora(codSem);
        } catch (DelecaoException de) {

            JOptionPane.showMessageDialog(panelButtons, de.getMessage(), "Erro ao ecluir", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(panelButtons, de.getRTException().getMessage(), "Erro ao excluir", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void openDadosPeça(AlocacaoPeca peca) throws UnsupportedOperationException {

        if (peca == null) {

            throw new UnsupportedOperationException("Valor nulo");
        }

        tfDadosPeçaTipo.setText(peca.getItemPeca().getPeca().getTipo().toString());
        tfDadosPeçaFabricante.setText(peca.getItemPeca().getPeca().getFabricante());
        tfDadosPeçaAno.setText(String.valueOf(peca.getItemPeca().getAnoFab()));
        tfdadosPeçaTVUR.setText(String.valueOf(peca.getItemPeca().getTempoVidaUtilRestante()));
        tfDadosPeçaDataAquis.setText(DateConversion.dateToString(peca.getItemPeca().getDataAquis()));
        tfDadosPeçaDataAloc.setText(DateConversion.dateToString(peca.getDataInclusaoItemPeca()));

        centralizarInternalFrame(iFrameDadosPeça);
        iFrameDadosPeça.setVisible(true);
    }

    private void preencheTiposPeca(JComboBox cbTiposPeca) {

        for (TipoPeca tp : TipoPeca.values()) {

            ModelString msTipoPeca = new ModelString("" + tp.getCodTipoPeca() + " - " + tp, tp.getCodTipoPeca());
            cbTiposPeca.addItem(msTipoPeca);
        }
    }

    private void restauraCamposCadPeça() {

        cbCadPecaTipo.removeAllItems();
        preencheTiposPeca(cbCadPecaTipo);
        tfCadPecaCod.setText("" + cadItensPeca.geraCodigoItemPeca());
        ftfCadPecaDataReg.setText(DateConversion.dateToString(new Date()));
        tfCadPecaFabricante.setText("");
        ftfCadPecaAno.setText("");
        ftfCadPecaDataAquis.setText("");
        spnQtd.setValue(1);
        tfCadPecaFabricante.requestFocus();
    }

    private TipoPeca getTipoPeca(int codTipoPeca) {

        for (TipoPeca tp : TipoPeca.values()) {

            if (tp.getCodTipoPeca() == codTipoPeca) {

                return tp;
            }
        }

        return null;
    }

    private boolean incluiPeca() {

        TipoPeca tp = getTipoPeca(obtemCodDeObjetoLista(cbCadPecaTipo.getSelectedItem()));

        if (!verificaCampoVazio(tfCadPecaFabricante)) {

            return false;
        }

        Peca p = new Peca(tfCadPecaFabricante.getText(), tp);

        if (!verificaAno(ftfCadPecaAno) || !verificaData(ftfCadPecaDataAquis) || !verificaData(ftfCadPecaDataReg) || !verificaDataAno(Integer.parseInt(ftfCadPecaAno.getText()), DateConversion.stringToDate(ftfCadPecaDataAquis.getText()))) {

            return false;
        }

        ItemPeca ip;

        try {

            ip = new ItemPeca(Integer.parseInt(tfCadPecaCod.getText()), Integer.parseInt(ftfCadPecaAno.getText()), DateConversion.stringToDate(ftfCadPecaDataAquis.getText()), p, sldCadPecaTVUR.getValue());
        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {

            cadPeca.insertPeca(p);
        } catch (InsercaoException ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
            JOptionPane.showMessageDialog(null, ex.getRTException().getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        for (int i = 1; i <= Integer.parseInt(spnQtd.getValue().toString()); i++) {

            try {

                cadItensPeca.insertItemPeca(ip);
                ip.setIdentificacao(ip.getIdentificacao() + 1);
            } catch (InsercaoException ex) {

                JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(null, ex.getRTException().getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    public boolean verificaDataAno(int ano, Date data) {

        if (ano > (data.getYear() + 1900)) {

            JOptionPane.showMessageDialog(null, "Ano posterior à data de aquisição", "Inconsistência de dados", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    public void restauraCamposConsPeca() {

        preencheTiposPeca(cbConsPecaTipoPeca);
        ModelString mdConsFabricante = new ModelString("Fabricante", 0);
        ModelString mdConsData = new ModelString("Data de Aquisição", 1);
        ModelString mdConsAno = new ModelString("Ano", 2);
        cbConsPecaParamBusca.addItem(mdConsFabricante);
        cbConsPecaParamBusca.addItem(mdConsData);
        cbConsPecaParamBusca.addItem(mdConsAno);
        btConsPecaCancelar.setVisible(false);
        btConsPecaSalvar.setVisible(false);
        btConsPecaAlterar.setVisible(true);
        btConsPecaExcluir.setVisible(true);
        tfConsPecaCod.setEditable(false);
        tfConsPecaTipo.setEditable(false);
        habilitaCamposEditPeca(false);
        habilitaCamposBucaPeca(true);
        limpaCamposConsPeca();
        tfConsPecaBusca.requestFocus();
    }

    public void limpaCamposConsPeca() {

        tfConsPecaCod.setText("");
        tfConsPecaFabricante.setText("");
        tfConsPecaTipo.setText("");
        ftfConsPecaAno.setText("");
        ftfConsPecaDataAquis.setText("");
        sldConsPecaTVUR.setMaximum(100);
        sldConsPecaTVUR.setValue(50);
    }

    public void preencheListaPecas(JList list, Collection<ItemPeca> pecas) {

        DefaultListModel dlmPecas = new DefaultListModel();

        for (ItemPeca ip : pecas) {

            ModelString msPeca = new ModelString("" + ip.getIdentificacao() + " - " + ip.getPeca().getFabricante() + " - " + ip.getTempoVidaUtilRestante(), ip.getIdentificacao());
            dlmPecas.addElement(msPeca);
        }

        list.setModel(dlmPecas);
    }

    public Collection<ItemPeca> buscaPecas(int paramBusca, Object valorParam) {

        Collection<ItemPeca> itens = new LinkedList<ItemPeca>();

        switch (paramBusca) {

            case 0: {

                for (ItemPeca ip : CadastroItensPeca.itensPeca.values()) {

                    if (ip.getPeca().getFabricante().contains(valorParam.toString())) {

                        itens.add(ip);
                    }
                }

                break;
            }

            case 1: {

                for (ItemPeca ip : CadastroItensPeca.itensPeca.values()) {

                    if (DateConversion.dateToString(ip.getDataAquis()).contains(valorParam.toString())) {

                        itens.add(ip);
                    }
                }

                break;
            }

            case 2: {

                for (ItemPeca ip : CadastroItensPeca.itensPeca.values()) {

                    if (String.valueOf(ip.getAnoFab()).equals(valorParam.toString())) {

                        itens.add(ip);
                    }
                }

                break;
            }
        }

        return itens;
    }

    public void preencheDadosPeca(ItemPeca ip) {

        tfConsPecaCod.setText("" + ip.getIdentificacao());
        tfConsPecaFabricante.setText(ip.getPeca().getFabricante());
        tfConsPecaTipo.setText(ip.getPeca().getTipo().toString());
        ftfConsPecaAno.setText("" + ip.getAnoFab());
        ftfConsPecaDataAquis.setText(DateConversion.dateToString(ip.getDataAquis()));
        sldConsPecaTVUR.setMaximum(ip.getPeca().getTipo().getEstVidaUtil());
        sldConsPecaTVUR.setValue(ip.getTempoVidaUtilRestante());
    }

    public void habilitaCamposEditPeca(boolean habil) {

        tfConsPecaFabricante.setEditable(habil);
        ftfConsPecaAno.setEditable(habil);
        ftfConsPecaDataAquis.setEditable(habil);
        sldConsPecaTVUR.setEnabled(habil);
    }

    public void habilitaCamposBucaPeca(boolean habil) {

        cbConsPecaParamBusca.setEnabled(habil);
        cbConsPecaTipoPeca.setEnabled(habil);
        tfConsPecaBusca.setEditable(habil);
        listConsPecaPecas.setEnabled(habil);
    }
    
    private void invertBotoesConsPeca(){
        
        btConsPecaAlterar.setVisible(!btConsPecaAlterar.isVisible());
        btConsPecaCancelar.setVisible(!btConsPecaCancelar.isVisible());
        btConsPecaExcluir.setVisible(!btConsPecaExcluir.isVisible());
        btConsPecaSalvar.setVisible(!btConsPecaSalvar.isVisible());
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jDesktopPane1 = new javax.swing.JDesktopPane();
        iFrameCadSemeadora = new javax.swing.JInternalFrame();
        panelButtons = new javax.swing.JPanel();
        iFrameCadSemBTConcluir = new javax.swing.JButton();
        iFrameCadSemBTCancelar = new javax.swing.JButton();
        panelArea = new javax.swing.JPanel();
        panelDadosSemeadora = new javax.swing.JPanel();
        lbCodSem = new javax.swing.JLabel();
        lbModSem = new javax.swing.JLabel();
        tfCodSem = new javax.swing.JTextField();
        lbMarcaSem = new javax.swing.JLabel();
        tfMarcaSem = new javax.swing.JTextField();
        lbAnoSem = new javax.swing.JLabel();
        ftfAnoSem = new javax.swing.JFormattedTextField();
        lbDataRegistro = new javax.swing.JLabel();
        ftfDataRegistro = new javax.swing.JFormattedTextField();
        tfModelo = new javax.swing.JTextField();
        panelDivisoesSemeadora = new javax.swing.JPanel();
        LbDiscoDosador = new javax.swing.JLabel();
        cbDiscoDosador = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        listDivisoes = new javax.swing.JList();
        btAddDivisao = new javax.swing.JButton();
        btAddDisco = new javax.swing.JButton();
        btCadSemReloadDiscoDosador = new javax.swing.JButton();
        iFrameAddDivisao = new javax.swing.JInternalFrame();
        panelPecasDivisao = new javax.swing.JPanel();
        lbDiscoCortePalhada = new javax.swing.JLabel();
        cbDiscoCortePalhada = new javax.swing.JComboBox();
        lbEjetorSementes = new javax.swing.JLabel();
        cbEjetorSementes = new javax.swing.JComboBox();
        lbPonteira = new javax.swing.JLabel();
        cbPonteira = new javax.swing.JComboBox();
        lbRolamento = new javax.swing.JLabel();
        cbRolamento = new javax.swing.JComboBox();
        lbDosadorFertilizante = new javax.swing.JLabel();
        cbDosadorFertilizante = new javax.swing.JComboBox();
        lbDiscoDuploTosado = new javax.swing.JLabel();
        cbDiscoDuploTosado = new javax.swing.JComboBox();
        btAddDiscoCortePalhada = new javax.swing.JButton();
        btAddEjetorSementes = new javax.swing.JButton();
        btAddPonteira = new javax.swing.JButton();
        btAddRolamento = new javax.swing.JButton();
        btAddDosadorFertilizante = new javax.swing.JButton();
        btAddDiscoDuploTosado = new javax.swing.JButton();
        btDivSemReloadDCortePalhada = new javax.swing.JButton();
        btDivSemReloadEjSemente = new javax.swing.JButton();
        btDivSemReloadPonteira = new javax.swing.JButton();
        btDivSemReloadRolamento = new javax.swing.JButton();
        btDivSemReloadDosFertilizante = new javax.swing.JButton();
        btDivSemReloadDDuploTosado = new javax.swing.JButton();
        panelDivisaoButtons = new javax.swing.JPanel();
        btIncluirDivisao = new javax.swing.JButton();
        btCancelarDivisao = new javax.swing.JButton();
        iFrameConsultaSemeadora = new javax.swing.JInternalFrame();
        panelButtonsConsultaSemeadora = new javax.swing.JPanel();
        btConsultaSemSalvar = new javax.swing.JButton();
        btConsultaSemAlterar = new javax.swing.JButton();
        btConsultaSemExcluir = new javax.swing.JButton();
        btConsultaSemCancelar = new javax.swing.JButton();
        panelPesquisarSemeadora = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listConsultaSemSemeadoras = new javax.swing.JList();
        lbConsultaSemSemeadora = new javax.swing.JLabel();
        cbConsultaSemTipoCons = new javax.swing.JComboBox();
        lbConsultaSemPesquisar = new javax.swing.JLabel();
        tfConsultaSemeadoraPesquisar = new javax.swing.JTextField();
        panelConsultaSemeadora = new javax.swing.JPanel();
        panelDadosSemeadoraConsulta = new javax.swing.JPanel();
        lbCodSemConsulta = new javax.swing.JLabel();
        lbModSemConsulta = new javax.swing.JLabel();
        tfCodSemConsulta = new javax.swing.JTextField();
        lbMarcaSemConsulta = new javax.swing.JLabel();
        tfMarcaSemConsulta = new javax.swing.JTextField();
        lbAnoSemConsulta = new javax.swing.JLabel();
        ftfAnoSemConsulta = new javax.swing.JFormattedTextField();
        lbDataRegistroConsulta = new javax.swing.JLabel();
        ftfDataRegistroConsulta = new javax.swing.JFormattedTextField();
        tfModeloConsulta = new javax.swing.JTextField();
        panelDivPecasSemeadora = new javax.swing.JPanel();
        lbConsultaSemDivisao = new javax.swing.JLabel();
        lbConsultaSemPeça = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listConsultaSemDivisoes = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        listConsultaSemPeca = new javax.swing.JList();
        btConsultaSemDadosPeca = new javax.swing.JButton();
        iFrameDadosPeça = new javax.swing.JInternalFrame();
        lbDadosPeçaTipo = new javax.swing.JLabel();
        tfDadosPeçaTipo = new javax.swing.JTextField();
        lbDadosPeçaFabricante = new javax.swing.JLabel();
        tfDadosPeçaFabricante = new javax.swing.JTextField();
        lbDadosPeçaAno = new javax.swing.JLabel();
        tfDadosPeçaAno = new javax.swing.JTextField();
        lbDadosPeçaDataAquis = new javax.swing.JLabel();
        tfDadosPeçaDataAquis = new javax.swing.JTextField();
        lbDadosPeçaTVUR = new javax.swing.JLabel();
        tfdadosPeçaTVUR = new javax.swing.JTextField();
        btDadosPeçaSair = new javax.swing.JButton();
        lbDadosPecaDataAloc = new javax.swing.JLabel();
        tfDadosPeçaDataAloc = new javax.swing.JTextField();
        iFrameCadPeca = new javax.swing.JInternalFrame();
        panelCadPecaDados = new javax.swing.JPanel();
        lbCadPecaCod = new javax.swing.JLabel();
        tfCadPecaCod = new javax.swing.JTextField();
        lbCadPecaTipo = new javax.swing.JLabel();
        cbCadPecaTipo = new javax.swing.JComboBox();
        lbCadPecaDataAquis = new javax.swing.JLabel();
        ftfCadPecaDataAquis = new javax.swing.JFormattedTextField();
        lbCadPecaFabricante = new javax.swing.JLabel();
        tfCadPecaFabricante = new javax.swing.JTextField();
        lbCadPecaDataReg = new javax.swing.JLabel();
        ftfCadPecaDataReg = new javax.swing.JFormattedTextField();
        lbCadPecaTVUR = new javax.swing.JLabel();
        sldCadPecaTVUR = new javax.swing.JSlider();
        lbCadPecaQtd = new javax.swing.JLabel();
        lbCadPecaAno = new javax.swing.JLabel();
        ftfCadPecaAno = new javax.swing.JFormattedTextField();
        spnQtd = new javax.swing.JSpinner();
        lbCadPecaEstadoSldTVUR = new javax.swing.JLabel();
        panelCadPecaButtons = new javax.swing.JPanel();
        btCadPecaIncluir = new javax.swing.JButton();
        btCadPecaCancelar = new javax.swing.JButton();
        iFrameConsPeca = new javax.swing.JInternalFrame();
        panelConsPecaBuscaPeca = new javax.swing.JPanel();
        cbConsPecaTipoPeca = new javax.swing.JComboBox();
        cbConsPecaParamBusca = new javax.swing.JComboBox();
        tfConsPecaBusca = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        listConsPecaPecas = new javax.swing.JList();
        lbConsPecaTipoPeca = new javax.swing.JLabel();
        lbConsPecaParamBusca = new javax.swing.JLabel();
        lbConsPecaPecas = new javax.swing.JLabel();
        panelConsPecaButtons = new javax.swing.JPanel();
        btConsPecaSalvar = new javax.swing.JButton();
        btConsPecaAlterar = new javax.swing.JButton();
        btConsPecaExcluir = new javax.swing.JButton();
        btConsPecaCancelar = new javax.swing.JButton();
        panelConsPecaDadosPeca = new javax.swing.JPanel();
        lbConsPecaCod = new javax.swing.JLabel();
        tfConsPecaCod = new javax.swing.JTextField();
        lbConsPecaTipo = new javax.swing.JLabel();
        lbConsPecaDataAquis = new javax.swing.JLabel();
        ftfConsPecaDataAquis = new javax.swing.JFormattedTextField();
        lbConsPecaFabricante = new javax.swing.JLabel();
        tfConsPecaFabricante = new javax.swing.JTextField();
        lbConsPecaTVUR = new javax.swing.JLabel();
        lbConsPecaAno = new javax.swing.JLabel();
        ftfConsPecaAno = new javax.swing.JFormattedTextField();
        lbConsPecaEstadoSldTVUR = new javax.swing.JLabel();
        tfConsPecaTipo = new javax.swing.JTextField();
        sldConsPecaTVUR = new javax.swing.JSlider();
        mainMenu = new javax.swing.JMenuBar();
        menuSemeadoras = new javax.swing.JMenu();
        itemMenuAddSemeadora = new javax.swing.JMenuItem();
        itemMenuExcluiSemeadora = new javax.swing.JMenuItem();
        itemMenuAlterarSemeadora = new javax.swing.JMenuItem();
        itemMenuConsultarSemeadora = new javax.swing.JMenuItem();
        menuPeças = new javax.swing.JMenu();
        itemMenuAddPeça = new javax.swing.JMenuItem();
        itemMenuExcluirPeça = new javax.swing.JMenuItem();
        itemMenuAlterarPeça = new javax.swing.JMenuItem();
        itemMenuConsultarPeça = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MPS");
        setPreferredSize(new java.awt.Dimension(500, 500));

        iFrameCadSemeadora.setClosable(true);
        iFrameCadSemeadora.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        iFrameCadSemeadora.setIconifiable(true);
        iFrameCadSemeadora.setResizable(true);
        iFrameCadSemeadora.setTitle("Cadastro de Semeadora");
        iFrameCadSemeadora.setVisible(false);

        panelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 50, 5));

        iFrameCadSemBTConcluir.setText("Concluir");
        iFrameCadSemBTConcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iFrameCadSemBTConcluirActionPerformed(evt);
            }
        });
        panelButtons.add(iFrameCadSemBTConcluir);

        iFrameCadSemBTCancelar.setText("Cancelar");
        iFrameCadSemBTCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iFrameCadSemBTCancelarActionPerformed(evt);
            }
        });
        panelButtons.add(iFrameCadSemBTCancelar);

        iFrameCadSemeadora.getContentPane().add(panelButtons, java.awt.BorderLayout.PAGE_END);

        panelArea.setLayout(new java.awt.BorderLayout());

        panelDadosSemeadora.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da Semeadora"));
        panelDadosSemeadora.setLayout(new java.awt.GridBagLayout());

        lbCodSem.setText("Código");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panelDadosSemeadora.add(lbCodSem, gridBagConstraints);

        lbModSem.setText("Modelo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        panelDadosSemeadora.add(lbModSem, gridBagConstraints);

        tfCodSem.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadora.add(tfCodSem, gridBagConstraints);

        lbMarcaSem.setText("Marca");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panelDadosSemeadora.add(lbMarcaSem, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 175;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadora.add(tfMarcaSem, gridBagConstraints);

        lbAnoSem.setText("Ano");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadora.add(lbAnoSem, gridBagConstraints);

        try {
            ftfAnoSem.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadora.add(ftfAnoSem, gridBagConstraints);

        lbDataRegistro.setText("Data de Registro");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadora.add(lbDataRegistro, gridBagConstraints);

        try {
            ftfDataRegistro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftfDataRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ftfDataRegistroKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadora.add(ftfDataRegistro, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 175;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadora.add(tfModelo, gridBagConstraints);

        panelArea.add(panelDadosSemeadora, java.awt.BorderLayout.NORTH);

        panelDivisoesSemeadora.setBorder(javax.swing.BorderFactory.createTitledBorder("Divisões da Semeadora"));
        panelDivisoesSemeadora.setLayout(new java.awt.GridBagLayout());

        LbDiscoDosador.setText("Disco dosador");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDivisoesSemeadora.add(LbDiscoDosador, gridBagConstraints);

        cbDiscoDosador.setPreferredSize(new java.awt.Dimension(250, 24));
        cbDiscoDosador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDiscoDosadorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDivisoesSemeadora.add(cbDiscoDosador, gridBagConstraints);

        listDivisoes.setPreferredSize(new java.awt.Dimension(300, 100));
        jScrollPane1.setViewportView(listDivisoes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        panelDivisoesSemeadora.add(jScrollPane1, gridBagConstraints);

        btAddDivisao.setText("Adicionar Divisão");
        btAddDivisao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddDivisaoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        panelDivisoesSemeadora.add(btAddDivisao, gridBagConstraints);

        btAddDisco.setText("Adicionar Item");
        btAddDisco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddDiscoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        panelDivisoesSemeadora.add(btAddDisco, gridBagConstraints);

        btCadSemReloadDiscoDosador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Button Reload.png"))); // NOI18N
        btCadSemReloadDiscoDosador.setToolTipText("Listar novamente os Discos Dosadores. Use-o quando inserir um novo item.");
        btCadSemReloadDiscoDosador.setPreferredSize(new java.awt.Dimension(40, 27));
        btCadSemReloadDiscoDosador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCadSemReloadDiscoDosadorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelDivisoesSemeadora.add(btCadSemReloadDiscoDosador, gridBagConstraints);

        panelArea.add(panelDivisoesSemeadora, java.awt.BorderLayout.CENTER);

        iFrameCadSemeadora.getContentPane().add(panelArea, java.awt.BorderLayout.CENTER);

        iFrameCadSemeadora.setBounds(60, 0, 580, 410);
        jDesktopPane1.add(iFrameCadSemeadora, javax.swing.JLayeredPane.DEFAULT_LAYER);

        iFrameAddDivisao.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        iFrameAddDivisao.setIconifiable(true);
        iFrameAddDivisao.setResizable(true);
        iFrameAddDivisao.setTitle("Linha de Semeadura");
        iFrameAddDivisao.setVisible(false);

        panelPecasDivisao.setBorder(javax.swing.BorderFactory.createTitledBorder("Peças da Linha"));
        panelPecasDivisao.setLayout(new java.awt.GridBagLayout());

        lbDiscoCortePalhada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDiscoCortePalhada.setText("Disco de Corte de Palhada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panelPecasDivisao.add(lbDiscoCortePalhada, gridBagConstraints);

        cbDiscoCortePalhada.setPreferredSize(new java.awt.Dimension(250, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panelPecasDivisao.add(cbDiscoCortePalhada, gridBagConstraints);

        lbEjetorSementes.setText("Ejetor de Sementes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panelPecasDivisao.add(lbEjetorSementes, gridBagConstraints);

        cbEjetorSementes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        cbEjetorSementes.setPreferredSize(new java.awt.Dimension(250, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panelPecasDivisao.add(cbEjetorSementes, gridBagConstraints);

        lbPonteira.setText("Ponteira");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panelPecasDivisao.add(lbPonteira, gridBagConstraints);

        cbPonteira.setPreferredSize(new java.awt.Dimension(250, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panelPecasDivisao.add(cbPonteira, gridBagConstraints);

        lbRolamento.setText("Rolamento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panelPecasDivisao.add(lbRolamento, gridBagConstraints);

        cbRolamento.setPreferredSize(new java.awt.Dimension(250, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panelPecasDivisao.add(cbRolamento, gridBagConstraints);

        lbDosadorFertilizante.setText("Dosador de Fertilizante");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panelPecasDivisao.add(lbDosadorFertilizante, gridBagConstraints);

        cbDosadorFertilizante.setPreferredSize(new java.awt.Dimension(250, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panelPecasDivisao.add(cbDosadorFertilizante, gridBagConstraints);

        lbDiscoDuploTosado.setText("Disco Duplo de Tosado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panelPecasDivisao.add(lbDiscoDuploTosado, gridBagConstraints);

        cbDiscoDuploTosado.setPreferredSize(new java.awt.Dimension(250, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panelPecasDivisao.add(cbDiscoDuploTosado, gridBagConstraints);

        btAddDiscoCortePalhada.setText("Adicionar Item");
        btAddDiscoCortePalhada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddDiscoCortePalhadaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btAddDiscoCortePalhada, gridBagConstraints);

        btAddEjetorSementes.setText("Adicionar Item");
        btAddEjetorSementes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddEjetorSementesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btAddEjetorSementes, gridBagConstraints);

        btAddPonteira.setText("Adicionar Item");
        btAddPonteira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddPonteiraActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btAddPonteira, gridBagConstraints);

        btAddRolamento.setText("Adicionar Item");
        btAddRolamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddRolamentoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btAddRolamento, gridBagConstraints);

        btAddDosadorFertilizante.setText("Adicionar Item");
        btAddDosadorFertilizante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddDosadorFertilizanteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btAddDosadorFertilizante, gridBagConstraints);

        btAddDiscoDuploTosado.setText("Adicionar Item");
        btAddDiscoDuploTosado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddDiscoDuploTosadoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btAddDiscoDuploTosado, gridBagConstraints);

        btDivSemReloadDCortePalhada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Button Reload.png"))); // NOI18N
        btDivSemReloadDCortePalhada.setPreferredSize(new java.awt.Dimension(40, 27));
        btDivSemReloadDCortePalhada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDivSemReloadDCortePalhadaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btDivSemReloadDCortePalhada, gridBagConstraints);

        btDivSemReloadEjSemente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Button Reload.png"))); // NOI18N
        btDivSemReloadEjSemente.setPreferredSize(new java.awt.Dimension(40, 27));
        btDivSemReloadEjSemente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDivSemReloadEjSementeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btDivSemReloadEjSemente, gridBagConstraints);

        btDivSemReloadPonteira.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Button Reload.png"))); // NOI18N
        btDivSemReloadPonteira.setPreferredSize(new java.awt.Dimension(40, 27));
        btDivSemReloadPonteira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDivSemReloadPonteiraActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btDivSemReloadPonteira, gridBagConstraints);

        btDivSemReloadRolamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Button Reload.png"))); // NOI18N
        btDivSemReloadRolamento.setPreferredSize(new java.awt.Dimension(40, 27));
        btDivSemReloadRolamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDivSemReloadRolamentoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btDivSemReloadRolamento, gridBagConstraints);

        btDivSemReloadDosFertilizante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Button Reload.png"))); // NOI18N
        btDivSemReloadDosFertilizante.setPreferredSize(new java.awt.Dimension(40, 27));
        btDivSemReloadDosFertilizante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDivSemReloadDosFertilizanteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btDivSemReloadDosFertilizante, gridBagConstraints);

        btDivSemReloadDDuploTosado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Button Reload.png"))); // NOI18N
        btDivSemReloadDDuploTosado.setPreferredSize(new java.awt.Dimension(40, 27));
        btDivSemReloadDDuploTosado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDivSemReloadDDuploTosadoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPecasDivisao.add(btDivSemReloadDDuploTosado, gridBagConstraints);

        iFrameAddDivisao.getContentPane().add(panelPecasDivisao, java.awt.BorderLayout.CENTER);

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING, 15, 5);
        flowLayout1.setAlignOnBaseline(true);
        panelDivisaoButtons.setLayout(flowLayout1);

        btIncluirDivisao.setText("Incluir");
        btIncluirDivisao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btIncluirDivisaoActionPerformed(evt);
            }
        });
        panelDivisaoButtons.add(btIncluirDivisao);

        btCancelarDivisao.setText("Cancelar");
        btCancelarDivisao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarDivisaoActionPerformed(evt);
            }
        });
        panelDivisaoButtons.add(btCancelarDivisao);

        iFrameAddDivisao.getContentPane().add(panelDivisaoButtons, java.awt.BorderLayout.SOUTH);

        iFrameAddDivisao.setBounds(110, 10, 480, 440);
        jDesktopPane1.add(iFrameAddDivisao, javax.swing.JLayeredPane.DEFAULT_LAYER);

        iFrameConsultaSemeadora.setClosable(true);
        iFrameConsultaSemeadora.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        iFrameConsultaSemeadora.setIconifiable(true);
        iFrameConsultaSemeadora.setResizable(true);
        iFrameConsultaSemeadora.setTitle("Consultar Semeadora");
        iFrameConsultaSemeadora.setPreferredSize(new java.awt.Dimension(850, 600));
        iFrameConsultaSemeadora.setVisible(false);
        iFrameConsultaSemeadora.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                iFrameConsultaSemeadoraInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        panelButtonsConsultaSemeadora.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 20, 5));

        btConsultaSemSalvar.setText("Salvar");
        btConsultaSemSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConsultaSemSalvarActionPerformed(evt);
            }
        });
        panelButtonsConsultaSemeadora.add(btConsultaSemSalvar);

        btConsultaSemAlterar.setText("Alterar");
        btConsultaSemAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConsultaSemAlterarActionPerformed(evt);
            }
        });
        panelButtonsConsultaSemeadora.add(btConsultaSemAlterar);

        btConsultaSemExcluir.setText("Excluir");
        btConsultaSemExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConsultaSemExcluirActionPerformed(evt);
            }
        });
        panelButtonsConsultaSemeadora.add(btConsultaSemExcluir);

        btConsultaSemCancelar.setText("Cancelar");
        btConsultaSemCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConsultaSemCancelarActionPerformed(evt);
            }
        });
        panelButtonsConsultaSemeadora.add(btConsultaSemCancelar);

        iFrameConsultaSemeadora.getContentPane().add(panelButtonsConsultaSemeadora, java.awt.BorderLayout.SOUTH);

        panelPesquisarSemeadora.setBorder(javax.swing.BorderFactory.createTitledBorder("Pesquisar Semeadora"));
        panelPesquisarSemeadora.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setPreferredSize(new java.awt.Dimension(280, 200));

        listConsultaSemSemeadoras.setPreferredSize(new java.awt.Dimension(350, 40));
        listConsultaSemSemeadoras.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listConsultaSemSemeadorasValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(listConsultaSemSemeadoras);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        panelPesquisarSemeadora.add(jScrollPane2, gridBagConstraints);

        lbConsultaSemSemeadora.setText("Semeadora");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelPesquisarSemeadora.add(lbConsultaSemSemeadora, gridBagConstraints);

        cbConsultaSemTipoCons.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Identificação", "Modelo", "Marca", "Ano" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelPesquisarSemeadora.add(cbConsultaSemTipoCons, gridBagConstraints);

        lbConsultaSemPesquisar.setText("Pesquisar:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPesquisarSemeadora.add(lbConsultaSemPesquisar, gridBagConstraints);

        tfConsultaSemeadoraPesquisar.setColumns(10);
        tfConsultaSemeadoraPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfConsultaSemeadoraPesquisarKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelPesquisarSemeadora.add(tfConsultaSemeadoraPesquisar, gridBagConstraints);

        iFrameConsultaSemeadora.getContentPane().add(panelPesquisarSemeadora, java.awt.BorderLayout.WEST);

        panelConsultaSemeadora.setLayout(new java.awt.BorderLayout());

        panelDadosSemeadoraConsulta.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da Semeadora"));
        panelDadosSemeadoraConsulta.setLayout(new java.awt.GridBagLayout());

        lbCodSemConsulta.setText("Código");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panelDadosSemeadoraConsulta.add(lbCodSemConsulta, gridBagConstraints);

        lbModSemConsulta.setText("Modelo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        panelDadosSemeadoraConsulta.add(lbModSemConsulta, gridBagConstraints);

        tfCodSemConsulta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(tfCodSemConsulta, gridBagConstraints);

        lbMarcaSemConsulta.setText("Marca");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panelDadosSemeadoraConsulta.add(lbMarcaSemConsulta, gridBagConstraints);

        tfMarcaSemConsulta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 175;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(tfMarcaSemConsulta, gridBagConstraints);

        lbAnoSemConsulta.setText("Ano");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(lbAnoSemConsulta, gridBagConstraints);

        ftfAnoSemConsulta.setEditable(false);
        try {
            ftfAnoSemConsulta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(ftfAnoSemConsulta, gridBagConstraints);

        lbDataRegistroConsulta.setText("Data de Registro");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(lbDataRegistroConsulta, gridBagConstraints);

        ftfDataRegistroConsulta.setEditable(false);
        try {
            ftfDataRegistroConsulta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftfDataRegistroConsulta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ftfDataRegistroConsultaKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(ftfDataRegistroConsulta, gridBagConstraints);

        tfModeloConsulta.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 175;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(tfModeloConsulta, gridBagConstraints);

        panelConsultaSemeadora.add(panelDadosSemeadoraConsulta, java.awt.BorderLayout.NORTH);

        panelDivPecasSemeadora.setBorder(javax.swing.BorderFactory.createTitledBorder("Divisões e Peças"));
        panelDivPecasSemeadora.setLayout(new java.awt.GridBagLayout());

        lbConsultaSemDivisao.setText("Divisão");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelDivPecasSemeadora.add(lbConsultaSemDivisao, gridBagConstraints);

        lbConsultaSemPeça.setText("Peça");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panelDivPecasSemeadora.add(lbConsultaSemPeça, gridBagConstraints);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(200, 200));

        listConsultaSemDivisoes.setPreferredSize(new java.awt.Dimension(200, 200));
        listConsultaSemDivisoes.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listConsultaSemDivisoesValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(listConsultaSemDivisoes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelDivPecasSemeadora.add(jScrollPane3, gridBagConstraints);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(200, 200));

        listConsultaSemPeca.setPreferredSize(new java.awt.Dimension(250, 200));
        listConsultaSemPeca.setValueIsAdjusting(true);
        jScrollPane4.setViewportView(listConsultaSemPeca);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelDivPecasSemeadora.add(jScrollPane4, gridBagConstraints);

        btConsultaSemDadosPeca.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        btConsultaSemDadosPeca.setText("Dados da Peça");
        btConsultaSemDadosPeca.setPreferredSize(new java.awt.Dimension(120, 25));
        btConsultaSemDadosPeca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConsultaSemDadosPecaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panelDivPecasSemeadora.add(btConsultaSemDadosPeca, gridBagConstraints);

        panelConsultaSemeadora.add(panelDivPecasSemeadora, java.awt.BorderLayout.CENTER);

        iFrameConsultaSemeadora.getContentPane().add(panelConsultaSemeadora, java.awt.BorderLayout.CENTER);

        iFrameConsultaSemeadora.setBounds(-110, 0, 890, 530);
        jDesktopPane1.add(iFrameConsultaSemeadora, javax.swing.JLayeredPane.DEFAULT_LAYER);

        iFrameDadosPeça.setClosable(true);
        iFrameDadosPeça.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        iFrameDadosPeça.setIconifiable(true);
        iFrameDadosPeça.setResizable(true);
        iFrameDadosPeça.setTitle("Dados da Peça");
        iFrameDadosPeça.setVisible(false);
        iFrameDadosPeça.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                iFrameDadosPeçaInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        iFrameDadosPeça.getContentPane().setLayout(new java.awt.GridBagLayout());

        lbDadosPeçaTipo.setText("Tipo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 0, 0);
        iFrameDadosPeça.getContentPane().add(lbDadosPeçaTipo, gridBagConstraints);

        tfDadosPeçaTipo.setEditable(false);
        tfDadosPeçaTipo.setPreferredSize(new java.awt.Dimension(200, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 0, 0);
        iFrameDadosPeça.getContentPane().add(tfDadosPeçaTipo, gridBagConstraints);

        lbDadosPeçaFabricante.setText("Fabricante:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 0);
        iFrameDadosPeça.getContentPane().add(lbDadosPeçaFabricante, gridBagConstraints);

        tfDadosPeçaFabricante.setEditable(false);
        tfDadosPeçaFabricante.setPreferredSize(new java.awt.Dimension(150, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        iFrameDadosPeça.getContentPane().add(tfDadosPeçaFabricante, gridBagConstraints);

        lbDadosPeçaAno.setText("Ano:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 0);
        iFrameDadosPeça.getContentPane().add(lbDadosPeçaAno, gridBagConstraints);

        tfDadosPeçaAno.setEditable(false);
        tfDadosPeçaAno.setPreferredSize(new java.awt.Dimension(70, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        iFrameDadosPeça.getContentPane().add(tfDadosPeçaAno, gridBagConstraints);

        lbDadosPeçaDataAquis.setText("Data de Aquisição: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(15, 12, 0, 0);
        iFrameDadosPeça.getContentPane().add(lbDadosPeçaDataAquis, gridBagConstraints);

        tfDadosPeçaDataAquis.setEditable(false);
        tfDadosPeçaDataAquis.setPreferredSize(new java.awt.Dimension(100, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        iFrameDadosPeça.getContentPane().add(tfDadosPeçaDataAquis, gridBagConstraints);

        lbDadosPeçaTVUR.setText("Tempo de Vida Útil Restante:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 0);
        iFrameDadosPeça.getContentPane().add(lbDadosPeçaTVUR, gridBagConstraints);

        tfdadosPeçaTVUR.setEditable(false);
        tfdadosPeçaTVUR.setPreferredSize(new java.awt.Dimension(50, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        iFrameDadosPeça.getContentPane().add(tfdadosPeçaTVUR, gridBagConstraints);

        btDadosPeçaSair.setText("Sair");
        btDadosPeçaSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDadosPeçaSairActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        iFrameDadosPeça.getContentPane().add(btDadosPeçaSair, gridBagConstraints);

        lbDadosPecaDataAloc.setText("Data de Alocação:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 0);
        iFrameDadosPeça.getContentPane().add(lbDadosPecaDataAloc, gridBagConstraints);

        tfDadosPeçaDataAloc.setEditable(false);
        tfDadosPeçaDataAloc.setPreferredSize(new java.awt.Dimension(100, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        iFrameDadosPeça.getContentPane().add(tfDadosPeçaDataAloc, gridBagConstraints);

        iFrameDadosPeça.setBounds(20, 70, 610, 310);
        jDesktopPane1.add(iFrameDadosPeça, javax.swing.JLayeredPane.DEFAULT_LAYER);

        iFrameCadPeca.setClosable(true);
        iFrameCadPeca.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        iFrameCadPeca.setIconifiable(true);
        iFrameCadPeca.setResizable(true);
        iFrameCadPeca.setTitle("Cadastro de Peças");
        iFrameCadPeca.setVisible(false);

        panelCadPecaDados.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panelCadPecaDados.setLayout(new java.awt.GridBagLayout());

        lbCadPecaCod.setText("Código:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(14, 12, 0, 0);
        panelCadPecaDados.add(lbCadPecaCod, gridBagConstraints);

        tfCadPecaCod.setEditable(false);
        tfCadPecaCod.setPreferredSize(new java.awt.Dimension(40, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 0, 0);
        panelCadPecaDados.add(tfCadPecaCod, gridBagConstraints);

        lbCadPecaTipo.setText("Tipo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(19, 12, 0, 0);
        panelCadPecaDados.add(lbCadPecaTipo, gridBagConstraints);

        cbCadPecaTipo.setPreferredSize(new java.awt.Dimension(250, 27));
        cbCadPecaTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbCadPecaTipoItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        panelCadPecaDados.add(cbCadPecaTipo, gridBagConstraints);

        lbCadPecaDataAquis.setText("Data de Aquisição:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(17, 12, 0, 0);
        panelCadPecaDados.add(lbCadPecaDataAquis, gridBagConstraints);

        try {
            ftfCadPecaDataAquis.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftfCadPecaDataAquis.setPreferredSize(new java.awt.Dimension(100, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        panelCadPecaDados.add(ftfCadPecaDataAquis, gridBagConstraints);

        lbCadPecaFabricante.setText("Fabricante:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(19, 10, 0, 0);
        panelCadPecaDados.add(lbCadPecaFabricante, gridBagConstraints);

        tfCadPecaFabricante.setPreferredSize(new java.awt.Dimension(150, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 5, 0, 0);
        panelCadPecaDados.add(tfCadPecaFabricante, gridBagConstraints);

        lbCadPecaDataReg.setText("Data de Registro:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(17, 10, 0, 0);
        panelCadPecaDados.add(lbCadPecaDataReg, gridBagConstraints);

        try {
            ftfCadPecaDataReg.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftfCadPecaDataReg.setPreferredSize(new java.awt.Dimension(100, 27));
        ftfCadPecaDataReg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfCadPecaDataRegActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        panelCadPecaDados.add(ftfCadPecaDataReg, gridBagConstraints);

        lbCadPecaTVUR.setText("Tempo de vida útil restante:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 12, 0, 0);
        panelCadPecaDados.add(lbCadPecaTVUR, gridBagConstraints);

        sldCadPecaTVUR.setMajorTickSpacing(50);
        sldCadPecaTVUR.setMinorTickSpacing(1);
        sldCadPecaTVUR.setPaintLabels(true);
        sldCadPecaTVUR.setPaintTicks(true);
        sldCadPecaTVUR.setSnapToTicks(true);
        sldCadPecaTVUR.setPreferredSize(new java.awt.Dimension(520, 42));
        sldCadPecaTVUR.setValueIsAdjusting(true);
        sldCadPecaTVUR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldCadPecaTVURStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        panelCadPecaDados.add(sldCadPecaTVUR, gridBagConstraints);

        lbCadPecaQtd.setText("Quantidade:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(17, 12, 0, 0);
        panelCadPecaDados.add(lbCadPecaQtd, gridBagConstraints);

        lbCadPecaAno.setText("Ano:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 0);
        panelCadPecaDados.add(lbCadPecaAno, gridBagConstraints);

        try {
            ftfCadPecaAno.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftfCadPecaAno.setPreferredSize(new java.awt.Dimension(60, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        panelCadPecaDados.add(ftfCadPecaAno, gridBagConstraints);

        spnQtd.setModel(new javax.swing.SpinnerNumberModel(1, 1, 30, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        panelCadPecaDados.add(spnQtd, gridBagConstraints);

        lbCadPecaEstadoSldTVUR.setPreferredSize(new java.awt.Dimension(40, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelCadPecaDados.add(lbCadPecaEstadoSldTVUR, gridBagConstraints);

        iFrameCadPeca.getContentPane().add(panelCadPecaDados, java.awt.BorderLayout.CENTER);

        panelCadPecaButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 15, 5));

        btCadPecaIncluir.setText("Incluir");
        btCadPecaIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCadPecaIncluirActionPerformed(evt);
            }
        });
        panelCadPecaButtons.add(btCadPecaIncluir);

        btCadPecaCancelar.setText("Cancelar");
        panelCadPecaButtons.add(btCadPecaCancelar);

        iFrameCadPeca.getContentPane().add(panelCadPecaButtons, java.awt.BorderLayout.PAGE_END);

        iFrameCadPeca.setBounds(-160, 30, 800, 370);
        jDesktopPane1.add(iFrameCadPeca, javax.swing.JLayeredPane.DEFAULT_LAYER);

        iFrameConsPeca.setClosable(true);
        iFrameConsPeca.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        iFrameConsPeca.setIconifiable(true);
        iFrameConsPeca.setResizable(true);
        iFrameConsPeca.setVisible(false);

        panelConsPecaBuscaPeca.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar Peça"));
        panelConsPecaBuscaPeca.setLayout(new java.awt.GridBagLayout());

        cbConsPecaTipoPeca.setPreferredSize(new java.awt.Dimension(250, 24));
        cbConsPecaTipoPeca.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbConsPecaTipoPecaItemStateChanged(evt);
            }
        });
        cbConsPecaTipoPeca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbConsPecaTipoPecaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panelConsPecaBuscaPeca.add(cbConsPecaTipoPeca, gridBagConstraints);

        cbConsPecaParamBusca.setPreferredSize(new java.awt.Dimension(150, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelConsPecaBuscaPeca.add(cbConsPecaParamBusca, gridBagConstraints);

        tfConsPecaBusca.setPreferredSize(new java.awt.Dimension(100, 27));
        tfConsPecaBusca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tfConsPecaBuscaMouseReleased(evt);
            }
        });
        tfConsPecaBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfConsPecaBuscaKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelConsPecaBuscaPeca.add(tfConsPecaBusca, gridBagConstraints);

        jScrollPane5.setPreferredSize(new java.awt.Dimension(259, 250));

        listConsPecaPecas.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listConsPecaPecasValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(listConsPecaPecas);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        panelConsPecaBuscaPeca.add(jScrollPane5, gridBagConstraints);

        lbConsPecaTipoPeca.setText("Tipo de Peça");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(11, 5, 0, 0);
        panelConsPecaBuscaPeca.add(lbConsPecaTipoPeca, gridBagConstraints);

        lbConsPecaParamBusca.setText("Parâmetro de Busca");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panelConsPecaBuscaPeca.add(lbConsPecaParamBusca, gridBagConstraints);

        lbConsPecaPecas.setText("Peças");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panelConsPecaBuscaPeca.add(lbConsPecaPecas, gridBagConstraints);

        iFrameConsPeca.getContentPane().add(panelConsPecaBuscaPeca, java.awt.BorderLayout.WEST);

        panelConsPecaButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 15, 5));

        btConsPecaSalvar.setText("Salvar");
        panelConsPecaButtons.add(btConsPecaSalvar);

        btConsPecaAlterar.setText("Alterar");
        btConsPecaAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConsPecaAlterarActionPerformed(evt);
            }
        });
        panelConsPecaButtons.add(btConsPecaAlterar);

        btConsPecaExcluir.setText("Excluir");
        panelConsPecaButtons.add(btConsPecaExcluir);

        btConsPecaCancelar.setText("Cancelar");
        btConsPecaCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConsPecaCancelarActionPerformed(evt);
            }
        });
        panelConsPecaButtons.add(btConsPecaCancelar);

        iFrameConsPeca.getContentPane().add(panelConsPecaButtons, java.awt.BorderLayout.SOUTH);

        panelConsPecaDadosPeca.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da Peça"));
        panelConsPecaDadosPeca.setLayout(new java.awt.GridBagLayout());

        lbConsPecaCod.setText("Código:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(14, 12, 0, 0);
        panelConsPecaDadosPeca.add(lbConsPecaCod, gridBagConstraints);

        tfConsPecaCod.setEditable(false);
        tfConsPecaCod.setPreferredSize(new java.awt.Dimension(40, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 0, 0);
        panelConsPecaDadosPeca.add(tfConsPecaCod, gridBagConstraints);

        lbConsPecaTipo.setText("Tipo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(19, 12, 0, 0);
        panelConsPecaDadosPeca.add(lbConsPecaTipo, gridBagConstraints);

        lbConsPecaDataAquis.setText("Data de Aquisição:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(17, 12, 0, 0);
        panelConsPecaDadosPeca.add(lbConsPecaDataAquis, gridBagConstraints);

        ftfConsPecaDataAquis.setEditable(false);
        try {
            ftfConsPecaDataAquis.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftfConsPecaDataAquis.setPreferredSize(new java.awt.Dimension(100, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        panelConsPecaDadosPeca.add(ftfConsPecaDataAquis, gridBagConstraints);

        lbConsPecaFabricante.setText("Fabricante:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(19, 10, 0, 0);
        panelConsPecaDadosPeca.add(lbConsPecaFabricante, gridBagConstraints);

        tfConsPecaFabricante.setEditable(false);
        tfConsPecaFabricante.setPreferredSize(new java.awt.Dimension(150, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 5, 0, 0);
        panelConsPecaDadosPeca.add(tfConsPecaFabricante, gridBagConstraints);

        lbConsPecaTVUR.setText("Tempo de vida útil restante:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 12, 0, 0);
        panelConsPecaDadosPeca.add(lbConsPecaTVUR, gridBagConstraints);

        lbConsPecaAno.setText("Ano:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 0);
        panelConsPecaDadosPeca.add(lbConsPecaAno, gridBagConstraints);

        ftfConsPecaAno.setEditable(false);
        try {
            ftfConsPecaAno.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftfConsPecaAno.setPreferredSize(new java.awt.Dimension(60, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        panelConsPecaDadosPeca.add(ftfConsPecaAno, gridBagConstraints);

        lbConsPecaEstadoSldTVUR.setPreferredSize(new java.awt.Dimension(40, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panelConsPecaDadosPeca.add(lbConsPecaEstadoSldTVUR, gridBagConstraints);

        tfConsPecaTipo.setEditable(false);
        tfConsPecaTipo.setPreferredSize(new java.awt.Dimension(250, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        panelConsPecaDadosPeca.add(tfConsPecaTipo, gridBagConstraints);

        sldConsPecaTVUR.setMajorTickSpacing(50);
        sldConsPecaTVUR.setMinorTickSpacing(1);
        sldConsPecaTVUR.setPaintLabels(true);
        sldConsPecaTVUR.setPaintTicks(true);
        sldConsPecaTVUR.setSnapToTicks(true);
        sldConsPecaTVUR.setPreferredSize(new java.awt.Dimension(520, 42));
        sldConsPecaTVUR.setValueIsAdjusting(true);
        sldConsPecaTVUR.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldConsPecaTVURStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 0, 0);
        panelConsPecaDadosPeca.add(sldConsPecaTVUR, gridBagConstraints);

        iFrameConsPeca.getContentPane().add(panelConsPecaDadosPeca, java.awt.BorderLayout.CENTER);

        iFrameConsPeca.setBounds(-500, 30, 1100, 500);
        jDesktopPane1.add(iFrameConsPeca, javax.swing.JLayeredPane.DEFAULT_LAYER);

        getContentPane().add(jDesktopPane1, java.awt.BorderLayout.CENTER);

        menuSemeadoras.setText("Semeadoras");

        itemMenuAddSemeadora.setText("Incluir Semeadora");
        itemMenuAddSemeadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuAddSemeadoraActionPerformed(evt);
            }
        });
        menuSemeadoras.add(itemMenuAddSemeadora);

        itemMenuExcluiSemeadora.setText("Excluir Semeadora");
        itemMenuExcluiSemeadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuExcluiSemeadoraActionPerformed(evt);
            }
        });
        menuSemeadoras.add(itemMenuExcluiSemeadora);

        itemMenuAlterarSemeadora.setText("Alterar Semeadora");
        itemMenuAlterarSemeadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuAlterarSemeadoraActionPerformed(evt);
            }
        });
        menuSemeadoras.add(itemMenuAlterarSemeadora);

        itemMenuConsultarSemeadora.setText("Consultar Semeadora");
        itemMenuConsultarSemeadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuConsultarSemeadoraActionPerformed(evt);
            }
        });
        menuSemeadoras.add(itemMenuConsultarSemeadora);

        mainMenu.add(menuSemeadoras);

        menuPeças.setText("Peças");

        itemMenuAddPeça.setText("Incluir Peça");
        itemMenuAddPeça.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuAddPeçaActionPerformed(evt);
            }
        });
        menuPeças.add(itemMenuAddPeça);

        itemMenuExcluirPeça.setText("Excluir Peça");
        itemMenuExcluirPeça.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuExcluirPeçaActionPerformed(evt);
            }
        });
        menuPeças.add(itemMenuExcluirPeça);

        itemMenuAlterarPeça.setText("Alterar Peça");
        itemMenuAlterarPeça.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuAlterarPeçaActionPerformed(evt);
            }
        });
        menuPeças.add(itemMenuAlterarPeça);

        itemMenuConsultarPeça.setText("Consultar Peça");
        itemMenuConsultarPeça.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuConsultarPeçaActionPerformed(evt);
            }
        });
        menuPeças.add(itemMenuConsultarPeça);

        mainMenu.add(menuPeças);

        setJMenuBar(mainMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void iFrameCadSemBTConcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iFrameCadSemBTConcluirActionPerformed

        incluirSemeadora();
    }//GEN-LAST:event_iFrameCadSemBTConcluirActionPerformed

    private void itemMenuAddSemeadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuAddSemeadoraActionPerformed

        abreFrameCadSemeadora();
    }//GEN-LAST:event_itemMenuAddSemeadoraActionPerformed

    private void ftfDataRegistroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftfDataRegistroKeyReleased
    }//GEN-LAST:event_ftfDataRegistroKeyReleased

    private void cbDiscoDosadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDiscoDosadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbDiscoDosadorActionPerformed

    private void iFrameCadSemBTCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iFrameCadSemBTCancelarActionPerformed

        ajustaCamposCadSemeadora();
        iFrameCadSemeadora.setVisible(false);
    }//GEN-LAST:event_iFrameCadSemBTCancelarActionPerformed

    private void btAddDiscoCortePalhadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddDiscoCortePalhadaActionPerformed

        if (!iFrameCadPeca.isVisible()) {

            centralizarInternalFrame(iFrameCadPeca);
            iFrameCadPeca.setVisible(true);
            restauraCamposCadPeça();

            selectTipo(TipoPeca.DISCO_DE_CORTE_DE_PALHADA);
        }
    }//GEN-LAST:event_btAddDiscoCortePalhadaActionPerformed

    private void btAddDivisaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddDivisaoActionPerformed

        abreFrameAddDivisao();
        itemMenuAddSemeadora.setEnabled(false);
        iFrameCadSemeadora.setVisible(false);
    }//GEN-LAST:event_btAddDivisaoActionPerformed

    private void btIncluirDivisaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btIncluirDivisaoActionPerformed

        if (incluirDivisaoLinha()) {

            iFrameAddDivisao.setVisible(false);
            iFrameCadSemeadora.setVisible(true);
            itemMenuAddSemeadora.setEnabled(true);
            refreshListaDivisoes(semeadora, listDivisoes);
        }
    }//GEN-LAST:event_btIncluirDivisaoActionPerformed

    private void btCancelarDivisaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarDivisaoActionPerformed

        iFrameAddDivisao.setVisible(false);
        iFrameCadSemeadora.setVisible(true);
        itemMenuAddSemeadora.setEnabled(true);
    }//GEN-LAST:event_btCancelarDivisaoActionPerformed

    private void ftfDataRegistroConsultaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ftfDataRegistroConsultaKeyReleased
    }//GEN-LAST:event_ftfDataRegistroConsultaKeyReleased

    private void itemMenuExcluiSemeadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuExcluiSemeadoraActionPerformed

        abreFrameConsultaSem();
    }//GEN-LAST:event_itemMenuExcluiSemeadoraActionPerformed

    private void itemMenuAlterarSemeadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuAlterarSemeadoraActionPerformed

        abreFrameConsultaSem();
    }//GEN-LAST:event_itemMenuAlterarSemeadoraActionPerformed

    private void itemMenuConsultarSemeadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuConsultarSemeadoraActionPerformed

        abreFrameConsultaSem();
    }//GEN-LAST:event_itemMenuConsultarSemeadoraActionPerformed

    private void tfConsultaSemeadoraPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfConsultaSemeadoraPesquisarKeyReleased

        if ("".equals(tfConsultaSemeadoraPesquisar.getText())) {
            cadSem.listSemeadoras();
            preencheListaSems(CadastroSemeadoras.semeadoras.values());
        } else {

            preencheListaSems(consultaSemeadoras((String) cbConsultaSemTipoCons.getSelectedItem(), tfConsultaSemeadoraPesquisar.getText()));
        }
        iFrameConsultaSemeadora.validate();
        iFrameConsultaSemeadora.repaint();
    }//GEN-LAST:event_tfConsultaSemeadoraPesquisarKeyReleased

    private void listConsultaSemSemeadorasValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listConsultaSemSemeadorasValueChanged

        if (listConsultaSemSemeadoras.getSelectedIndex() != -1) {

            preencheCamposConsultaSemeadora(obtemSemeadora(listConsultaSemSemeadoras.getSelectedValue()));
            listConsultaSemPeca.setModel(new DefaultListModel());
        } else {

            limpaCamposConsultaSem();
        }
    }//GEN-LAST:event_listConsultaSemSemeadorasValueChanged

    private void listConsultaSemDivisoesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listConsultaSemDivisoesValueChanged

        if (listConsultaSemDivisoes.getSelectedIndex() != -1) {

            Semeadora sem = obtemSemeadora(listConsultaSemSemeadoras.getSelectedValue());
            preencheListaPeças(sem.selecionarDivisao(obtemCodDeObjetoLista(listConsultaSemDivisoes.getSelectedValue())).listarPecas(), listConsultaSemPeca);
        }
    }//GEN-LAST:event_listConsultaSemDivisoesValueChanged

    private void btConsultaSemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsultaSemSalvarActionPerformed

        if (JOptionPane.showConfirmDialog(null, "Confirma a alteração?", "Alteração", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION && atualizarSemeadora()) {

            JOptionPane.showMessageDialog(null, "Semeadora atualizada com sucesso!", "Atualização", JOptionPane.INFORMATION_MESSAGE);
            restauraCamposConsultaSem();
        }
    }//GEN-LAST:event_btConsultaSemSalvarActionPerformed

    private void btConsultaSemAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsultaSemAlterarActionPerformed

        if (listConsultaSemSemeadoras.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(null, "É necessário selecionar uma semeadora antes", "Semeadora não selecionada", JOptionPane.WARNING_MESSAGE);
        } else {

            btConsultaSemSalvar.setVisible(true);
            btConsultaSemCancelar.setVisible(true);
            btConsultaSemExcluir.setVisible(false);
            btConsultaSemAlterar.setVisible(false);
            tfConsultaSemeadoraPesquisar.setEnabled(false);
            listConsultaSemSemeadoras.setEnabled(false);
            ftfDataRegistroConsulta.setText(DateConversion.dateToString(new Date()));
            habilitaCamposAlteracaoSemeadora(true);
            ftfAnoSemConsulta.requestFocus();
        }
    }//GEN-LAST:event_btConsultaSemAlterarActionPerformed

    private void btConsultaSemCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsultaSemCancelarActionPerformed

        restauraCamposConsultaSem();
    }//GEN-LAST:event_btConsultaSemCancelarActionPerformed

    private void btConsultaSemExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsultaSemExcluirActionPerformed

        if (listConsultaSemSemeadoras.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(null, "É necessário selecionar uma semeadora antes", "Semeadora não selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(null, "Confirma a exclusão? "
                + "\nExcluir a semadora implicará na perda de registros de atividades e manuteções!",
                "Exlusão", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION && excluiSemeadora()) {

            JOptionPane.showMessageDialog(null, "Semeadora excluída com sucesso", "Exclusão", JOptionPane.INFORMATION_MESSAGE);
            restauraCamposConsultaSem();
        }
    }//GEN-LAST:event_btConsultaSemExcluirActionPerformed

    private void iFrameConsultaSemeadoraInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_iFrameConsultaSemeadoraInternalFrameClosing

        CadastroSemeadoras.semeadoras.clear();
    }//GEN-LAST:event_iFrameConsultaSemeadoraInternalFrameClosing

    private void btDadosPeçaSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDadosPeçaSairActionPerformed

        itemMenuAlterarSemeadora.setEnabled(true);
        itemMenuConsultarSemeadora.setEnabled(true);
        itemMenuExcluiSemeadora.setEnabled(true);
        iFrameConsultaSemeadora.setVisible(true);
        iFrameDadosPeça.setVisible(false);
    }//GEN-LAST:event_btDadosPeçaSairActionPerformed

    private void iFrameDadosPeçaInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_iFrameDadosPeçaInternalFrameClosing

        btDadosPeçaSairActionPerformed(null);
    }//GEN-LAST:event_iFrameDadosPeçaInternalFrameClosing

    private void btConsultaSemDadosPecaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsultaSemDadosPecaActionPerformed

        if (listConsultaSemPeca.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(null, "Deve-se selecionar uma peça antes!", "Peça não selecionada", JOptionPane.WARNING_MESSAGE);
        } else {

            itemMenuAlterarSemeadora.setEnabled(false);
            itemMenuConsultarSemeadora.setEnabled(false);
            itemMenuExcluiSemeadora.setEnabled(false);
            iFrameConsultaSemeadora.setVisible(false);

            int codSem = obtemCodDeObjetoLista(listConsultaSemSemeadoras.getSelectedValue());
            int codPeca = obtemCodDeObjetoLista(listConsultaSemPeca.getSelectedValue());
            openDadosPeça(CadastroSemeadoras.semeadoras.get(codSem).selecionarPeca(codPeca));
        }
    }//GEN-LAST:event_btConsultaSemDadosPecaActionPerformed

    private void itemMenuAddPeçaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuAddPeçaActionPerformed

        centralizarInternalFrame(iFrameCadPeca);
        if (!iFrameCadPeca.isVisible()) {

            iFrameCadPeca.setVisible(true);
            restauraCamposCadPeça();
        }
    }//GEN-LAST:event_itemMenuAddPeçaActionPerformed

    private void btCadPecaIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCadPecaIncluirActionPerformed

        if (incluiPeca()) {

            JOptionPane.showMessageDialog(null, "Peça(s) incluída(s) com sucesso!", "Inclusão de Peça", JOptionPane.INFORMATION_MESSAGE);
            restauraCamposCadPeça();
        }
    }//GEN-LAST:event_btCadPecaIncluirActionPerformed

    private void cbCadPecaTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbCadPecaTipoItemStateChanged

        if (cbCadPecaTipo.getSelectedIndex() != -1) {

            TipoPeca tp = getTipoPeca(obtemCodDeObjetoLista(cbCadPecaTipo.getSelectedItem()));
            sldCadPecaTVUR.setMaximum(tp.getEstVidaUtil());
            sldCadPecaTVUR.setValue(tp.getEstVidaUtil());
        }
    }//GEN-LAST:event_cbCadPecaTipoItemStateChanged

    private void ftfCadPecaDataRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfCadPecaDataRegActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ftfCadPecaDataRegActionPerformed

    private void sldCadPecaTVURStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldCadPecaTVURStateChanged

        sldCadPecaTVUR.setToolTipText("" + sldCadPecaTVUR.getValue());
        lbCadPecaEstadoSldTVUR.setText("" + sldCadPecaTVUR.getValue());
    }//GEN-LAST:event_sldCadPecaTVURStateChanged

    private void btCadSemReloadDiscoDosadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCadSemReloadDiscoDosadorActionPerformed

        preencheComboPecas(cbDiscoDosador, TipoPeca.DISCO_DOSADOR);
    }//GEN-LAST:event_btCadSemReloadDiscoDosadorActionPerformed

    private void btAddDiscoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddDiscoActionPerformed

        if (!iFrameCadPeca.isVisible()) {

            centralizarInternalFrame(iFrameCadPeca);
            iFrameCadPeca.setVisible(true);
            restauraCamposCadPeça();

            selectTipo(TipoPeca.DISCO_DOSADOR);
        }
    }//GEN-LAST:event_btAddDiscoActionPerformed

    private void btDivSemReloadPonteiraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDivSemReloadPonteiraActionPerformed

        preencheComboPecas(cbPonteira, TipoPeca.PONTEIRA);
    }//GEN-LAST:event_btDivSemReloadPonteiraActionPerformed

    private void btDivSemReloadDCortePalhadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDivSemReloadDCortePalhadaActionPerformed

        preencheComboPecas(cbDiscoCortePalhada, TipoPeca.DISCO_DE_CORTE_DE_PALHADA);
    }//GEN-LAST:event_btDivSemReloadDCortePalhadaActionPerformed

    private void btDivSemReloadEjSementeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDivSemReloadEjSementeActionPerformed

        preencheComboPecas(cbEjetorSementes, TipoPeca.EJETOR_DE_SEMENTES);
    }//GEN-LAST:event_btDivSemReloadEjSementeActionPerformed

    private void btDivSemReloadRolamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDivSemReloadRolamentoActionPerformed

        preencheComboPecas(cbRolamento, TipoPeca.ROLAMENTO);
    }//GEN-LAST:event_btDivSemReloadRolamentoActionPerformed

    private void btDivSemReloadDosFertilizanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDivSemReloadDosFertilizanteActionPerformed

        preencheComboPecas(cbDosadorFertilizante, TipoPeca.DOSADOR_DE_FERTILIZANTE);
    }//GEN-LAST:event_btDivSemReloadDosFertilizanteActionPerformed

    private void btDivSemReloadDDuploTosadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDivSemReloadDDuploTosadoActionPerformed

        preencheComboPecas(cbDiscoDuploTosado, TipoPeca.DISCO_DUPLO_DE_TOSADO);
    }//GEN-LAST:event_btDivSemReloadDDuploTosadoActionPerformed

    private void btAddEjetorSementesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddEjetorSementesActionPerformed

        if (!iFrameCadPeca.isVisible()) {

            centralizarInternalFrame(iFrameCadPeca);
            iFrameCadPeca.setVisible(true);
            restauraCamposCadPeça();

            selectTipo(TipoPeca.EJETOR_DE_SEMENTES);
        }
    }//GEN-LAST:event_btAddEjetorSementesActionPerformed

    private void btAddPonteiraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddPonteiraActionPerformed

        if (!iFrameCadPeca.isVisible()) {

            centralizarInternalFrame(iFrameCadPeca);
            iFrameCadPeca.setVisible(true);
            restauraCamposCadPeça();

            selectTipo(TipoPeca.PONTEIRA);
        }
    }//GEN-LAST:event_btAddPonteiraActionPerformed

    private void btAddRolamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddRolamentoActionPerformed

        if (!iFrameCadPeca.isVisible()) {

            centralizarInternalFrame(iFrameCadPeca);
            iFrameCadPeca.setVisible(true);
            restauraCamposCadPeça();

            selectTipo(TipoPeca.ROLAMENTO);
        }
    }//GEN-LAST:event_btAddRolamentoActionPerformed

    private void btAddDosadorFertilizanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddDosadorFertilizanteActionPerformed

        if (!iFrameCadPeca.isVisible()) {

            centralizarInternalFrame(iFrameCadPeca);
            iFrameCadPeca.setVisible(true);
            restauraCamposCadPeça();

            selectTipo(TipoPeca.DOSADOR_DE_FERTILIZANTE);
        }
    }//GEN-LAST:event_btAddDosadorFertilizanteActionPerformed

    private void btAddDiscoDuploTosadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddDiscoDuploTosadoActionPerformed

        if (!iFrameCadPeca.isVisible()) {

            centralizarInternalFrame(iFrameCadPeca);
            iFrameCadPeca.setVisible(true);
            restauraCamposCadPeça();

            selectTipo(TipoPeca.DISCO_DUPLO_DE_TOSADO);
        }
    }//GEN-LAST:event_btAddDiscoDuploTosadoActionPerformed

    private void cbConsPecaTipoPecaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbConsPecaTipoPecaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbConsPecaTipoPecaActionPerformed

    private void itemMenuExcluirPeçaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuExcluirPeçaActionPerformed

        if (!iFrameConsPeca.isVisible()) {

            centralizarInternalFrame(iFrameConsPeca);
            restauraCamposConsPeca();
            cadItensPeca.selectItensPecaNaoAlocadosByTipo(getTipoPeca(obtemCodDeObjetoLista(cbConsPecaTipoPeca.getSelectedItem())));
            preencheListaPecas(listConsPecaPecas, CadastroItensPeca.itensPeca.values());
            iFrameConsPeca.setVisible(true);
            tfConsPecaBusca.requestFocus();
        }
    }//GEN-LAST:event_itemMenuExcluirPeçaActionPerformed

    private void itemMenuAlterarPeçaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuAlterarPeçaActionPerformed

        if (!iFrameConsPeca.isVisible()) {

            centralizarInternalFrame(iFrameConsPeca);
            restauraCamposConsPeca();
            cadItensPeca.selectItensPecaNaoAlocadosByTipo(getTipoPeca(obtemCodDeObjetoLista(cbConsPecaTipoPeca.getSelectedItem())));
            preencheListaPecas(listConsPecaPecas, CadastroItensPeca.itensPeca.values());
            iFrameConsPeca.setVisible(true);
            tfConsPecaBusca.requestFocus();
        }
    }//GEN-LAST:event_itemMenuAlterarPeçaActionPerformed

    private void itemMenuConsultarPeçaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuConsultarPeçaActionPerformed

        if (!iFrameConsPeca.isVisible()) {

            centralizarInternalFrame(iFrameConsPeca);
            restauraCamposConsPeca();
            cadItensPeca.selectItensPecaNaoAlocadosByTipo(getTipoPeca(obtemCodDeObjetoLista(cbConsPecaTipoPeca.getSelectedItem())));
            preencheListaPecas(listConsPecaPecas, CadastroItensPeca.itensPeca.values());
            iFrameConsPeca.setVisible(true);
            tfConsPecaBusca.requestFocus();
        }
    }//GEN-LAST:event_itemMenuConsultarPeçaActionPerformed

    private void sldConsPecaTVURStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldConsPecaTVURStateChanged
        
        lbConsPecaEstadoSldTVUR.setText("" + sldConsPecaTVUR.getValue());
    }//GEN-LAST:event_sldConsPecaTVURStateChanged

    private void cbConsPecaTipoPecaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbConsPecaTipoPecaItemStateChanged

        cadItensPeca.selectItensPecaNaoAlocadosByTipo(getTipoPeca(obtemCodDeObjetoLista(cbConsPecaTipoPeca.getSelectedItem())));
        preencheListaPecas(listConsPecaPecas, CadastroItensPeca.itensPeca.values());
    }//GEN-LAST:event_cbConsPecaTipoPecaItemStateChanged

    private void tfConsPecaBuscaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfConsPecaBuscaMouseReleased
    }//GEN-LAST:event_tfConsPecaBuscaMouseReleased

    private void tfConsPecaBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfConsPecaBuscaKeyReleased

        if ("".equals(tfConsPecaBusca.getText())) {

            cadItensPeca.selectItensPecaNaoAlocadosByTipo(getTipoPeca(obtemCodDeObjetoLista(cbConsPecaTipoPeca.getSelectedItem())));
            preencheListaPecas(listConsPecaPecas, CadastroItensPeca.itensPeca.values());
        } else {

            preencheListaPecas(listConsPecaPecas, buscaPecas(obtemCodDeObjetoLista(cbConsPecaParamBusca.getSelectedItem()), tfConsPecaBusca.getText()));
        }
    }//GEN-LAST:event_tfConsPecaBuscaKeyReleased

    private void listConsPecaPecasValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listConsPecaPecasValueChanged

        if (listConsPecaPecas.getSelectedIndex() != -1) {

            preencheDadosPeca(CadastroItensPeca.itensPeca.get(obtemCodDeObjetoLista(listConsPecaPecas.getSelectedValue())));
        }
    }//GEN-LAST:event_listConsPecaPecasValueChanged

    private void btConsPecaAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsPecaAlterarActionPerformed
        
        if(listConsPecaPecas.getSelectedIndex() != -1){
            
            habilitaCamposEditPeca(true);
            habilitaCamposBucaPeca(false);
            invertBotoesConsPeca();
        }else{
            
            JOptionPane.showMessageDialog(null, "Deve-se selecionar um item antes!", "Item não selecionado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btConsPecaAlterarActionPerformed

    private void btConsPecaCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsPecaCancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btConsPecaCancelarActionPerformed

    private void selectTipo(TipoPeca tp) {

        for (int i = 0; i < cbCadPecaTipo.getModel().getSize(); i++) {

            Object obj = cbCadPecaTipo.getModel().getElementAt(i);

            if (obj instanceof ModelString) {

                if (((ModelString) obj).getCodigo() == tp.getCodTipoPeca()) {

                    cbCadPecaTipo.setSelectedIndex(i);
                }
            }
        }
    }

    private class PBPainter implements Painter<JComponent> {

        private Color color;

        public PBPainter(Color color) {

            this.color = color;
        }

        @Override
        public void paint(Graphics2D g, JComponent object, int width, int height) {
            GradientPaint gradPaint;

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gradPaint = new GradientPaint((width / 2.0f), 0, color.brighter(), (width / 2.0f), (height / 2.0f), color.darker(), true);
            g.setPaint(gradPaint);
            g.fillRect(2, 2, (width - 5), (height - 5));

            /*Color outline = new Color(0, 85, 0);
             g.setColor(outline);
             g.drawRect(2, 2, (width - 5), (height - 5));
             Color trans = new Color(outline.getRed(), outline.getGreen(), outline.getBlue(), 100);
             g.setColor(trans);
             g.drawRect(1, 1, (width - 3), (height - 3));*/
        }
    }

    private void changeProgressbarColors(JProgressBar pb, final Color color) {

        UIDefaults defaults = new UIDefaults();
        defaults.put("ProgressBar[Enabled].foregroundPainter", new PBPainter(color));
        defaults.put("ProgressBar[Enabled+Finished].foregroundPainter", new PBPainter(color));

        pb.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
        pb.putClientProperty("Nimbus.Overrides", defaults);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;


                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FramePrincipal().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LbDiscoDosador;
    private javax.swing.JButton btAddDisco;
    private javax.swing.JButton btAddDiscoCortePalhada;
    private javax.swing.JButton btAddDiscoDuploTosado;
    private javax.swing.JButton btAddDivisao;
    private javax.swing.JButton btAddDosadorFertilizante;
    private javax.swing.JButton btAddEjetorSementes;
    private javax.swing.JButton btAddPonteira;
    private javax.swing.JButton btAddRolamento;
    private javax.swing.JButton btCadPecaCancelar;
    private javax.swing.JButton btCadPecaIncluir;
    private javax.swing.JButton btCadSemReloadDiscoDosador;
    private javax.swing.JButton btCancelarDivisao;
    private javax.swing.JButton btConsPecaAlterar;
    private javax.swing.JButton btConsPecaCancelar;
    private javax.swing.JButton btConsPecaExcluir;
    private javax.swing.JButton btConsPecaSalvar;
    private javax.swing.JButton btConsultaSemAlterar;
    private javax.swing.JButton btConsultaSemCancelar;
    private javax.swing.JButton btConsultaSemDadosPeca;
    private javax.swing.JButton btConsultaSemExcluir;
    private javax.swing.JButton btConsultaSemSalvar;
    private javax.swing.JButton btDadosPeçaSair;
    private javax.swing.JButton btDivSemReloadDCortePalhada;
    private javax.swing.JButton btDivSemReloadDDuploTosado;
    private javax.swing.JButton btDivSemReloadDosFertilizante;
    private javax.swing.JButton btDivSemReloadEjSemente;
    private javax.swing.JButton btDivSemReloadPonteira;
    private javax.swing.JButton btDivSemReloadRolamento;
    private javax.swing.JButton btIncluirDivisao;
    private javax.swing.JComboBox cbCadPecaTipo;
    private javax.swing.JComboBox cbConsPecaParamBusca;
    private javax.swing.JComboBox cbConsPecaTipoPeca;
    private javax.swing.JComboBox cbConsultaSemTipoCons;
    private javax.swing.JComboBox cbDiscoCortePalhada;
    private javax.swing.JComboBox cbDiscoDosador;
    private javax.swing.JComboBox cbDiscoDuploTosado;
    private javax.swing.JComboBox cbDosadorFertilizante;
    private javax.swing.JComboBox cbEjetorSementes;
    private javax.swing.JComboBox cbPonteira;
    private javax.swing.JComboBox cbRolamento;
    private javax.swing.JFormattedTextField ftfAnoSem;
    private javax.swing.JFormattedTextField ftfAnoSemConsulta;
    private javax.swing.JFormattedTextField ftfCadPecaAno;
    private javax.swing.JFormattedTextField ftfCadPecaDataAquis;
    private javax.swing.JFormattedTextField ftfCadPecaDataReg;
    private javax.swing.JFormattedTextField ftfConsPecaAno;
    private javax.swing.JFormattedTextField ftfConsPecaDataAquis;
    private javax.swing.JFormattedTextField ftfDataRegistro;
    private javax.swing.JFormattedTextField ftfDataRegistroConsulta;
    private javax.swing.JInternalFrame iFrameAddDivisao;
    private javax.swing.JInternalFrame iFrameCadPeca;
    private javax.swing.JButton iFrameCadSemBTCancelar;
    private javax.swing.JButton iFrameCadSemBTConcluir;
    private javax.swing.JInternalFrame iFrameCadSemeadora;
    private javax.swing.JInternalFrame iFrameConsPeca;
    private javax.swing.JInternalFrame iFrameConsultaSemeadora;
    private javax.swing.JInternalFrame iFrameDadosPeça;
    private javax.swing.JMenuItem itemMenuAddPeça;
    private javax.swing.JMenuItem itemMenuAddSemeadora;
    private javax.swing.JMenuItem itemMenuAlterarPeça;
    private javax.swing.JMenuItem itemMenuAlterarSemeadora;
    private javax.swing.JMenuItem itemMenuConsultarPeça;
    private javax.swing.JMenuItem itemMenuConsultarSemeadora;
    private javax.swing.JMenuItem itemMenuExcluiSemeadora;
    private javax.swing.JMenuItem itemMenuExcluirPeça;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lbAnoSem;
    private javax.swing.JLabel lbAnoSemConsulta;
    private javax.swing.JLabel lbCadPecaAno;
    private javax.swing.JLabel lbCadPecaCod;
    private javax.swing.JLabel lbCadPecaDataAquis;
    private javax.swing.JLabel lbCadPecaDataReg;
    private javax.swing.JLabel lbCadPecaEstadoSldTVUR;
    private javax.swing.JLabel lbCadPecaFabricante;
    private javax.swing.JLabel lbCadPecaQtd;
    private javax.swing.JLabel lbCadPecaTVUR;
    private javax.swing.JLabel lbCadPecaTipo;
    private javax.swing.JLabel lbCodSem;
    private javax.swing.JLabel lbCodSemConsulta;
    private javax.swing.JLabel lbConsPecaAno;
    private javax.swing.JLabel lbConsPecaCod;
    private javax.swing.JLabel lbConsPecaDataAquis;
    private javax.swing.JLabel lbConsPecaEstadoSldTVUR;
    private javax.swing.JLabel lbConsPecaFabricante;
    private javax.swing.JLabel lbConsPecaParamBusca;
    private javax.swing.JLabel lbConsPecaPecas;
    private javax.swing.JLabel lbConsPecaTVUR;
    private javax.swing.JLabel lbConsPecaTipo;
    private javax.swing.JLabel lbConsPecaTipoPeca;
    private javax.swing.JLabel lbConsultaSemDivisao;
    private javax.swing.JLabel lbConsultaSemPesquisar;
    private javax.swing.JLabel lbConsultaSemPeça;
    private javax.swing.JLabel lbConsultaSemSemeadora;
    private javax.swing.JLabel lbDadosPecaDataAloc;
    private javax.swing.JLabel lbDadosPeçaAno;
    private javax.swing.JLabel lbDadosPeçaDataAquis;
    private javax.swing.JLabel lbDadosPeçaFabricante;
    private javax.swing.JLabel lbDadosPeçaTVUR;
    private javax.swing.JLabel lbDadosPeçaTipo;
    private javax.swing.JLabel lbDataRegistro;
    private javax.swing.JLabel lbDataRegistroConsulta;
    private javax.swing.JLabel lbDiscoCortePalhada;
    private javax.swing.JLabel lbDiscoDuploTosado;
    private javax.swing.JLabel lbDosadorFertilizante;
    private javax.swing.JLabel lbEjetorSementes;
    private javax.swing.JLabel lbMarcaSem;
    private javax.swing.JLabel lbMarcaSemConsulta;
    private javax.swing.JLabel lbModSem;
    private javax.swing.JLabel lbModSemConsulta;
    private javax.swing.JLabel lbPonteira;
    private javax.swing.JLabel lbRolamento;
    private javax.swing.JList listConsPecaPecas;
    private javax.swing.JList listConsultaSemDivisoes;
    private javax.swing.JList listConsultaSemPeca;
    private javax.swing.JList listConsultaSemSemeadoras;
    private javax.swing.JList listDivisoes;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JMenu menuPeças;
    private javax.swing.JMenu menuSemeadoras;
    private javax.swing.JPanel panelArea;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelButtonsConsultaSemeadora;
    private javax.swing.JPanel panelCadPecaButtons;
    private javax.swing.JPanel panelCadPecaDados;
    private javax.swing.JPanel panelConsPecaBuscaPeca;
    private javax.swing.JPanel panelConsPecaButtons;
    private javax.swing.JPanel panelConsPecaDadosPeca;
    private javax.swing.JPanel panelConsultaSemeadora;
    private javax.swing.JPanel panelDadosSemeadora;
    private javax.swing.JPanel panelDadosSemeadoraConsulta;
    private javax.swing.JPanel panelDivPecasSemeadora;
    private javax.swing.JPanel panelDivisaoButtons;
    private javax.swing.JPanel panelDivisoesSemeadora;
    private javax.swing.JPanel panelPecasDivisao;
    private javax.swing.JPanel panelPesquisarSemeadora;
    private javax.swing.JSlider sldCadPecaTVUR;
    private javax.swing.JSlider sldConsPecaTVUR;
    private javax.swing.JSpinner spnQtd;
    private javax.swing.JTextField tfCadPecaCod;
    private javax.swing.JTextField tfCadPecaFabricante;
    private javax.swing.JTextField tfCodSem;
    private javax.swing.JTextField tfCodSemConsulta;
    private javax.swing.JTextField tfConsPecaBusca;
    private javax.swing.JTextField tfConsPecaCod;
    private javax.swing.JTextField tfConsPecaFabricante;
    private javax.swing.JTextField tfConsPecaTipo;
    private javax.swing.JTextField tfConsultaSemeadoraPesquisar;
    private javax.swing.JTextField tfDadosPeçaAno;
    private javax.swing.JTextField tfDadosPeçaDataAloc;
    private javax.swing.JTextField tfDadosPeçaDataAquis;
    private javax.swing.JTextField tfDadosPeçaFabricante;
    private javax.swing.JTextField tfDadosPeçaTipo;
    private javax.swing.JTextField tfMarcaSem;
    private javax.swing.JTextField tfMarcaSemConsulta;
    private javax.swing.JTextField tfModelo;
    private javax.swing.JTextField tfModeloConsulta;
    private javax.swing.JTextField tfdadosPeçaTVUR;
    // End of variables declaration//GEN-END:variables
}
