package zt.tau.ui.component

// TODO: Make transparent
// @Composable
// fun DropTargetArea(
//    modifier: Modifier = Modifier,
//    dragEnter: (DropTargetDragEvent) -> Unit = { },
//    dragOver: (DropTargetDragEvent) -> Unit = { },
//    dropActionChanged: (DropTargetDragEvent) -> Unit = { },
//    dragExit: (DropTargetEvent) -> Unit = { },
//    drop: (DropTargetEvent) -> Unit = { },
//    content: @Composable () -> Unit
// ) {
//    Box(modifier) {
//        SwingPanel(
//            modifier = Modifier.matchParentSize(),
//            factory = {
//                ComposePanel().apply {
//                    dropTarget = DropTarget().apply {
//                        flavorMap = SystemFlavorMap.getDefaultFlavorMap()
//
//                        addDropTargetListener(object : DropTargetListener {
//                            override fun dragEnter(event: DropTargetDragEvent) = dragEnter(event)
//                            override fun dragOver(event: DropTargetDragEvent) = dragOver(event)
//                            override fun dropActionChanged(event: DropTargetDragEvent) = dropActionChanged(event)
//                            override fun dragExit(event: DropTargetEvent) = dragExit(event)
//                            override fun drop(event: DropTargetDropEvent) = drop(event)
//                        })
//                    }
//                }
//            }
//        )
//
//        content()
//    }
// }
