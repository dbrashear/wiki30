/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.gwt.wysiwyg.client.plugin.rt.dom.operation;

import org.xwiki.gwt.dom.client.Document;
import org.xwiki.gwt.dom.client.Element;
import org.xwiki.gwt.dom.client.Range;

import com.google.gwt.dom.client.Node;

import fr.loria.score.jupiter.tree.operation.TreeInsertParagraph;
import fr.loria.score.jupiter.tree.operation.TreeOperation;

/**
 * Applies {@link TreeInsertParagraph} on a DOM tree.
 * 
 * @version $Id$
 */
public class DomInsertParagraph extends AbstractDomOperation
{
    /**
     * Creates a new DOM operation equivalent to the given Tree operation.
     * 
     * @param operation a Tree operation
     */
    public DomInsertParagraph(TreeOperation operation)
    {
        super(operation);
    }

    @Override
    public Range execute(Document document)
    {
        Node targetNode = getTargetNode(document);
        Node container = domUtils.getNearestBlockContainer(targetNode);
        Node paragraph = document.createPElement();

        // Split the container at the specified position in the target node.
        Node caretContainer = domUtils.splitNode(container.getParentNode(), targetNode, getOperation().getPosition());
        if (caretContainer == container.getNextSibling()) {
            caretContainer = paragraph;
        }

        // Move the right side of the split to the new paragraph.
        paragraph.appendChild(Element.as(container.getNextSibling()).extractContents());
        container.getParentNode().replaceChild(paragraph, container.getNextSibling());

        // Make sure that both lines generated by the split can be edited.
        Element.as(container).ensureEditable();
        Element.as(paragraph).ensureEditable();

        // The caret should be placed at the start of the new paragraph.
        Range caret = document.createRange();
        caret.setStart(caretContainer, 0);
        caret.collapse(true);
        return caret;
    }
}
