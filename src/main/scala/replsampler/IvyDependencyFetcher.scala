package replsampler

import org.apache.ivy.Ivy
import DependencyFetcher.{Module => RSModule, ScalaVersion}
import IvyDependencyFetcher.Resolver
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.apache.ivy.plugins.resolver.{IBiblioResolver, DependencyResolver, ChainResolver}
import org.apache.ivy.core.settings.IvySettings
import org.apache.ivy.core.retrieve.RetrieveOptions
import org.apache.ivy.core.resolve.{IvyNode, ResolveOptions}
import java.io.File
import java.util.{List => JList}
import org.apache.ivy.plugins.parser.xml.XmlModuleDescriptorWriter
import org.apache.ivy.core.module.descriptor.{DefaultDependencyDescriptor, DefaultModuleDescriptor, ModuleDescriptor}
import org.apache.ivy.util.{DefaultMessageLogger, Message}
import scala.collection.JavaConversions._

case class IvyDependencyFetcher(scalaVersion: ScalaVersion, resolvers: Seq[Resolver] = Seq(Resolver.mavenCentral)) extends DependencyFetcher {
  private lazy val ivySettings = {
    val ivyResolvers = new ChainResolver
    ivyResolvers.setName("repl-sampler-all")
    resolvers.foreach(r => ivyResolvers.add(r))

    val settings = new IvySettings()
    settings.addResolver(ivyResolvers)
    settings.setDefaultResolver(ivyResolvers.getName)

    settings
  }

  def getAll(deps: Seq[RSModule]): Seq[File] = {
    val ivy = Ivy.newInstance(ivySettings)
    ivy.getLoggerEngine.setDefaultLogger(new DefaultMessageLogger(Message.MSG_WARN))

    val moduleDescriptor = DefaultModuleDescriptor
      .newDefaultInstance(ModuleRevisionId.newInstance("something", "caller", "working"))
    val depDescriptors = for (dep <- deps) yield {
      val module = ModuleRevisionId.newInstance(dep.group, dep.fullModule(scalaVersion), dep.version)
      new DefaultDependencyDescriptor(moduleDescriptor, module, false, false, true)
    }

    depDescriptors.foreach(moduleDescriptor.addDependency(_))

    val reportFile = File.createTempFile("ivy", ".xml")
    reportFile.deleteOnExit()
    XmlModuleDescriptorWriter.write(moduleDescriptor, reportFile)
    val report = ivy.resolve(reportFile)
    reportFile.delete()

    val resolvedDeps = report.getDependencies.asInstanceOf[JList[IvyNode]].toList

    val x = for {
      node <- resolvedDeps
      artifact <- node.getAllArtifacts
    } yield artifact
    println(x)

    Seq()
  }
}

object IvyDependencyFetcher {
  type Resolver = DependencyResolver

  object Resolver {
    val mavenCentral = {
      val x = new IBiblioResolver
      x.setM2compatible(true)
      x
    }
  }
}
