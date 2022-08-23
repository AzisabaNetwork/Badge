package net.testusuke.badge.components

/**
 * Created by testusuke on 2022/08/21
 * @author testusuke
 */
interface VisualComponentBuilder {

    /**
     * Inspect member parameters
     * If some error happen, throws Exception
     */
    fun assertParameters()

    /**
     * Build VisualComponent
     *
     * @return VisualComponent
     */
    fun build(): VisualComponent
}