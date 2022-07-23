package buildSVGTool

import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.batik.util.XMLResourceDescriptor
import org.w3c.dom.svg.SVGDocument
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

open class BuildSVGTool() {

    val namespaceURI = "http://www.w3.org/1999/xlink"
    lateinit var doc: SVGDocument

    constructor(file: String) : this() {
        val parser = XMLResourceDescriptor.getXMLParserClassName()
        doc = SAXSVGDocumentFactory(parser).createSVGDocument(file)
    }


    fun draw(): ByteArrayInputStream {
        val input = TranscoderInput(doc)
        val outputStream = ByteArrayOutputStream()
        val output = TranscoderOutput(outputStream)
        PNGTranscoder().transcode(input, output)
        outputStream.flush()
        outputStream.close()
        return ByteArrayInputStream(outputStream.toByteArray())
    }
}