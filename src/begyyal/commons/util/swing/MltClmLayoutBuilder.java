package begyyal.commons.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.google.common.collect.Lists;

import begyyal.commons.util.object.SuperList;
import begyyal.commons.util.object.SuperList.SuperListGen;

/**
 * 複数列でコンポーネントを配置するためのユーティリティ。<br>
 * 煩雑な{@link GroupLayout}の設定を当クラスにてとりまとめる。<br>
 */
public class MltClmLayoutBuilder {

    private final GroupLayout gl;

    private final SequentialGroup vsg;

    private final SuperList<ParallelGroup> hpg;

    private final List<Integer> colWidth;

    /**
     * 配置先のコンテナに対する背景色の設定無し。それ以外は関連事項を参照のこと。
     *
     * @see #MltClmLayoutBuilder(Container c, Color color, int colAmount, int[]
     *      colWidth)
     */
    public MltClmLayoutBuilder(Container c, int colAmount, int[] colWidth) {
        this(c, null, colAmount, colWidth);
    }

    /**
     * コンストラクタ。<br>
     * 列幅の配列の長さが列数を超過している場合は超過分がスルーされる。<br>
     * 逆に不足している場合は{@link GroupLayout#DEFAULT_SIZE}で代替される。
     *
     * @param c 配置先のコンテナ
     * @param color 背景色
     * @param colAmount 列数
     * @param colWidth 列幅
     */
    public MltClmLayoutBuilder(Container c, Color color, int colAmount, int[] colWidth) {

        this.colWidth = Lists.newArrayList();
        Container c2 = c;
        if (c instanceof JScrollPane) {
            c2 = new JPanel();
            ((JScrollPane) c).setViewportView(c2);
        }

        gl = new GroupLayout(c2);
        c2.setLayout(gl);
        if (color != null)
            c2.setBackground(color);

        vsg = gl.createSequentialGroup();
        gl.setVerticalGroup(vsg);

        hpg = IntStream.range(0, colAmount)
                .mapToObj(i -> {
                    this.colWidth.add(colWidth.length <= i ? null : colWidth[i]);
                    return gl.createParallelGroup(Alignment.LEADING);
                }).collect(SuperListGen.collect());
        SequentialGroup hsg = gl.createSequentialGroup();
        hpg.forEach(g -> hsg.addGroup(g));
        gl.setHorizontalGroup(hsg);
    }

    /**
     * 対象のコンポーネントを1行に配置する。<br>
     * コンポーネントの数が当クラスのコンストラクタにて指定した列数を超過する場合、超過分はスルーされる。
     *
     * @see #MltClmLayoutBuilder(Container c, Color color, int colAmount, int[]
     *      colWidth)
     */
    public void append(Component... components) {

        List<Component> compList = components.length > hpg.size()
                ? Arrays.asList(components).subList(0, hpg.size()) : Arrays.asList(components);

        ParallelGroup vpg = gl.createParallelGroup();
        vsg.addGroup(vpg);
        for (Component c : compList)
            vpg.addComponent(c,
                    GroupLayout.DEFAULT_SIZE,
                    GroupLayout.DEFAULT_SIZE,
                    GroupLayout.PREFERRED_SIZE);

        hpg.zip(compList, colWidth).forEach(tri -> {
            if (tri.getRight() == null)
                tri.getLeft().addComponent(tri.getMiddle());
            else
                tri.getLeft().addComponent(
                        tri.getMiddle(),
                        GroupLayout.DEFAULT_SIZE,
                        tri.getRight(),
                        GroupLayout.PREFERRED_SIZE);
        });
    }
}