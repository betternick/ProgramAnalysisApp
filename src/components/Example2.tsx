import React, { useState } from 'react'
import ReactFlow, {
  ReactFlowProvider,
  isNode,
  Node,
  useStoreState,
} from 'react-flow-renderer'
import dagre from 'dagre'


// higher order component (this component renders the entire page content in my case)
const TreeSearch: React.FC<TreeProps> = ({data}) => {

	const nodeWidth = 210
	const nodeHeight = 156

	  // slightly modified version of the function taken directly from the Layout-example: https://reactflow.dev/examples/layouting/ 
	const getLayoutedElements = (elements, direction = 'BT') => {
		const isHorizontal = direction === 'LR'
		dagreGraph.setGraph({ rankdir: direction })
	  
		elements.forEach((el) => {
		  if (typeof el != "boolean") {
			if (isNode(el)) {
			  dagreGraph.setNode(el.id, { width: nodeWidth, height: nodeHeight });
			} else {
			  dagreGraph.setEdge(el.source, el.target)
			}
		  }});
	  
		dagre.layout(dagreGraph)
	  
		return elements.map((el,idx) => {
		  if (typeof el != "boolean") {
		  if (isNode(el)) {
			const nodeWithPosition = dagreGraph.node(el.id)
			el.targetPosition = isHorizontal ? 'left' : 'bottom'
			el.sourcePosition = isHorizontal ? 'right' : 'top'
	  
			// unfortunately we need this little hack to pass a slighltiy different position
			// to notify react flow about the change. More over we are shifting the dagre node position
			// (anchor=center center) to the top left so it matches the react flow node anchor point (top left).
			el.position = {
			  x: nodeWithPosition.x - nodeWidth / 2 + Math.random() / 1000,
			  y: nodeWithPosition.y - nodeHeight / 2,
			};
		  }}
	  
		  return el
		});
	  }; 

	//initialElements = ... (your data)
	const layoutedElements = getLayoutedElements(initialElements); // default to static layout

	// helper function
	// slightly modified version of @mymywang's function
	const createGraphLayout = (graph, flowNodeStates: Node[], edges): Node[] => {

	  flowNodeStates.forEach((node) => {
		if (typeof node != "boolean") { // without this I experienced some weird behaviour, might not be necessary for your use case
		  if (isNode(node)) {
			graph.setNode(node.id, {
			  label: node.id,
			  width: node.__rf.width,
			  height: node.__rf.height,
			});
		  }
		}
	  });

	  edges.forEach((edge) => {
		graph.setEdge(edge.source, edge.target)
	  })

	  dagre.layout(graph)

	  return flowNodeStates.map((nodeState) => {
		const node = graph.node(nodeState.id)
		return {
		  ...nodeState,
		  position: {
			// The position from dagre layout is the center of the node.
			// Calculating the position of the top left corner for rendering.
			x: node.x - node.width / 2 + Math.random() / 1000,
			y: node.y - node.height / 2,
		  },
		};
	  });
	};

	// internal helper component that manages layouting/
	 const NodeLayouter = (props) => {
		const nodeStates = useStoreState((store) => store.nodes)
		const edgeStates = useStoreState((store) => store.edges) // store.nodes does not fetch edges, so this is needed to fetch edges
	  
		const nodeHasDimension = node => (node.__rf !=null && node.position != null)
	  
		const changeLayout = () => {
		  if (nodeStates.length>0 && nodeStates.every(nodeHasDimension)) {
			const elementsWithLayout = createGraphLayout(props.graph,nodeStates,edgeStates) // get dynamically layouted elements
			props.elementsSetter(elementsWithLayout.concat(edgeStates)) // concat edges and nodes together
		  }
		}

		  return (<button className="tightLayouterButton" onClick={()=>{changeLayout()}}>change layout</button>)
		}
	
	// flow component
	  const LayoutFlow = props => {
	    const [elements, setElements] = useState(layoutedElements);
		const flowStyles = {height: "30em"}
		// define onLoad, etc.
	 
		return (
		  <div className="layoutflow">
			<ReactFlowProvider>
			  <ReactFlow
				elements={elements}
				connectionLineType="smoothstep"
				style={flowStyles}
				/>
			  <Controls/>
			  <NodeLayouter graph={dagreGraph} elementsSetter={setElements}/>
			
			</ReactFlowProvider>
		  </div>
		);
	  };
	  
	  return (
				<...> // your page here
				<LayoutFlow/>
				</...>)