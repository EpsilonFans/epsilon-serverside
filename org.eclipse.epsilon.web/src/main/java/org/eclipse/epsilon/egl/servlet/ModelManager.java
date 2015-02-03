/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
******************************************************************************/
package org.eclipse.epsilon.egl.servlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.EmfUtil;
import org.eclipse.epsilon.emc.emf.xml.XmlModel;
import org.eclipse.epsilon.emc.graphml.GraphmlModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.eol.models.Model;
import org.eclipse.epsilon.eol.models.ModelRepository;

public class ModelManager {
	
	protected ServletContext servletContext;
	protected ModelRepository currentModelRepository;
	protected HashMap<String, IModel> cachedModels = new HashMap<String, IModel>();
	
	public void setCurrentModelRepository(ModelRepository currentModelRepository) {
		this.currentModelRepository = currentModelRepository;
	}
	
	public ModelRepository getCurrentModelRepository() {
		return currentModelRepository;
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public void loadMuddle(String name, String modelFile) throws EolModelLoadingException {
		
		IModel model = cachedModels.get(modelFile);
		
		if (model == null || !(model instanceof GraphmlModel)) {
			GraphmlModel graphmlModel = new GraphmlModel();
			model = graphmlModel;
			
			graphmlModel.setName(name);
			graphmlModel.setFile(new File(this.getServletContext().getRealPath(modelFile)));
			graphmlModel.load();
			cachedModels.put(name, graphmlModel);
		}
		
		getCurrentModelRepository().addModel(model); 
		
	}
	
	public void loadXml(String name, String modelFile) throws EolModelLoadingException {
		
		IModel model = cachedModels.get(modelFile);
		
		if (model == null || !(model instanceof XmlModel)) {
			XmlModel xmlModel = new XmlModel();
			model = xmlModel;
			
			xmlModel.setName(name);
			
			StringProperties properties = new StringProperties();
			properties.put(XmlModel.PROPERTY_NAME, name);
			properties.put(XmlModel.PROPERTY_MODEL_FILE, this.getServletContext().getRealPath(modelFile));
			properties.put(Model.PROPERTY_READONLOAD, "true");
			properties.put(Model.PROPERTY_STOREONDISPOSAL, "false");
			
			xmlModel.load(properties, "");
			cachedModels.put(name, xmlModel);
		}
		
		getCurrentModelRepository().addModel(model); 
		
	}	

	
	@SuppressWarnings("deprecation")
	public void loadModel(String name, String aliases, String modelFile, String metamodel, boolean expand, boolean metamodelIsFilebased) throws EolModelLoadingException {
		
		try {
			
			// System.out.println( modelFile );
			// System.out.println( getServletContext().getResource(modelFile).toString() );
			// System.out.println( URI.createURI(getServletContext().getResource(modelFile).toString()) );
			// System.out.println( this.getServletContext().getRealPath("/") );
			// System.out.println( URI.createFileURI(this.getServletContext().getRealPath("/")) );
			// System.out.println( URI.createFileURI(this.getServletContext().getRealPath("/")) );
			// System.out.println( URI.createURI(URI.createFileURI(this.getServletContext().getRealPath("/")) + modelFile) );
			// System.out.println( URI.createURI(modelFile).resolve(URI.createFileURI(this.getServletContext().getRealPath("/"))) );			
			// System.out.println(convertVirtualPathToAbsoluteFileURI(modelFile));
			
			IModel model = cachedModels.get(modelFile);
			
			if (model == null || !(model instanceof EmfModel)) {
				EmfModel emfModel = new EmfModel();
				model = emfModel;
				
				StringProperties properties = new StringProperties();
				properties.put(EmfModel.PROPERTY_NAME, name);
				properties.put(EmfModel.PROPERTY_ALIASES, aliases);
				properties.put(EmfModel.PROPERTY_EXPAND, expand + "");
				properties.put(EmfModel.PROPERTY_MODEL_URI, convertVirtualPathToAbsoluteFileURI(modelFile));
				if (!metamodelIsFilebased) {
					properties.put(EmfModel.PROPERTY_METAMODEL_URI, metamodel);
					properties.put(EmfModel.PROPERTY_IS_METAMODEL_FILE_BASED, "false");
				}
				else {
					properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, convertVirtualPathToAbsoluteFileURI(metamodel));
					properties.put(EmfModel.PROPERTY_IS_METAMODEL_FILE_BASED, "true");			
				}
				properties.put(EmfModel.PROPERTY_READONLOAD, "true");
				properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, "false");
							
				emfModel.load(properties, (IRelativePathResolver) null);
				cachedModels.put(modelFile, emfModel);
			}
			
			getCurrentModelRepository().addModel(model);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void save(String modelFile) {
		IModel model = cachedModels.get(modelFile);		
		if ( model != null ) {
			model.store();
		}
	}
	
	private URI convertVirtualPathToAbsoluteFileURI(String virtualPath) throws Exception {
		return URI.createURI(URI.createFileURI(this.getServletContext().getRealPath("/")) + "/" + virtualPath);
		// return URI.createURI(virtualPath).resolve(URI.createFileURI(this.getServletContext().getRealPath("/")));
	}

	public void loadModel(String name, String modelFile, String metamodelUri) throws EolModelLoadingException {
		loadModel(name, "", modelFile, metamodelUri, true, false);
	}
	
	public void loadModelByFile(String name, String modelFile, String metamodelFile) throws EolModelLoadingException {
		loadModel(name, "", modelFile, metamodelFile, true, true);
	}	
	
	protected ArrayList<String> registeredMetamodels = new ArrayList<String>(); 
	
	public void registerMetamodel(String metamodelFile) throws Exception {
		
		if (registeredMetamodels.contains(metamodelFile)) return;
		// EmfUtil.register(URI.createURI(getServletContext().getResource(metamodelFile).toString()), EPackage.Registry.INSTANCE);
		EmfUtil.register(convertVirtualPathToAbsoluteFileURI(metamodelFile), EPackage.Registry.INSTANCE);
		registeredMetamodels.add(metamodelFile);
	}
	
	public void uncacheModel(String modelFile) {
		cachedModels.remove(modelFile);
	}
	
	protected void reset() {
		cachedModels.clear();
		registeredMetamodels.clear();
	}
	
}
